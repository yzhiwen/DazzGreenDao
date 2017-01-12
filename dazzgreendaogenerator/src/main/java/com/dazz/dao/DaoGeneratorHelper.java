package com.dazz.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.daogenerator.ContentProvider;
import de.greenrobot.daogenerator.DaoUtil;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author yzw
 */

public class DaoGeneratorHelper {


    private Pattern patternKeepIncludes;
    private Pattern patternKeepFields;
    private Pattern patternKeepMethods;

    private Template templateDao;
    private Template templateDaoMaster;
    private Template templateDaoSession;
    private Template templateEntity;
    private Template templateContentProvider;

    public DaoGeneratorHelper() throws IOException {
        System.out.println("greenDAO Generator");
        System.out.println("Copyright 2011-2016 Markus Junginger, greenrobot.de. Licensed under GPL V3.");
        System.out.println("This program comes with ABSOLUTELY NO WARRANTY");

        patternKeepIncludes = compilePattern("INCLUDES");
        patternKeepFields = compilePattern("FIELDS");
        patternKeepMethods = compilePattern("METHODS");

        Configuration config = getConfiguration();

        templateDao = config.getTemplate("dao.ftl");
        templateDaoMaster = config.getTemplate("dao-master.ftl");
        templateDaoSession = config.getTemplate("dao-session.ftl");
        templateEntity = config.getTemplate("entity.ftl");
        templateContentProvider = config.getTemplate("content-provider.ftl");
    }

    private Configuration getConfiguration() throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        config.setClassForTemplateLoading(this.getClass(), "/");

        // When running from an IDE like IntelliJ, class loading resources may fail for some reason (Gradle is OK)
        // Working dir is module dir
        File dir = new File("dazzgreendaogenerator/src-template");
        if (!dir.exists()) {
            // Working dir is base module dir
            dir = new File("DaoGenerator/src/main/resources/");
        }
        config.setDirectoryForTemplateLoading(dir);
        return config;
    }

    private Pattern compilePattern(String sectionName) {
        int flags = Pattern.DOTALL | Pattern.MULTILINE;
        return Pattern.compile(".*^\\s*?//\\s*?KEEP " + sectionName + ".*?\n(.*?)^\\s*// KEEP " + sectionName
                + " END.*?\n", flags);
    }

    /**
     * Generates all entities and DAOs for the given schema.
     */
    public void generateAll(Schema schema, String outDir) throws Exception {
        generateAll(schema, outDir, null);
    }

    /**
     * Generates all entities and DAOs for the given schema.
     */
    public void generateAll(Schema schema, String outDir, String outDirEntity) throws Exception {
        long start = System.currentTimeMillis();

        File outDirFile = toFileForceExists(outDir);
        File outDirEntityFile = outDirEntity != null ? toFileForceExists(outDirEntity) : outDirFile;

        schema.init2ndPass();
        schema.init3rdPass();

        System.out.println("Processing schema version " + schema.getVersion() + "...");

        List<Entity> entities = schema.getEntities();
        for (Entity entity : entities) {
            generate(templateDao, outDirFile, entity.getJavaPackageDao(), entity.getClassNameDao(), schema, entity);
            if (!entity.isProtobuf() && !entity.isSkipGeneration()) {
                generate(templateEntity, outDirEntityFile, entity.getJavaPackage(), entity.getClassName(), schema, entity);
            }
            for (ContentProvider contentProvider : entity.getContentProviders()) {
                Map<String, Object> additionalObjectsForTemplate = new HashMap<String, Object>();
                additionalObjectsForTemplate.put("contentProvider", contentProvider);
                generate(templateContentProvider, outDirFile, entity.getJavaPackage(), entity.getClassName()
                        + "ContentProvider", schema, entity, additionalObjectsForTemplate);
            }
        }
        generate(templateDaoMaster, outDirFile, schema.getDefaultJavaPackageDao(), schema.getSchemaName() + "DaoMaster", schema, null);
        generate(templateDaoSession, outDirFile, schema.getDefaultJavaPackageDao(), schema.getSchemaName() + "DaoSession", schema, null);

        long time = System.currentTimeMillis() - start;
        System.out.println("Processed " + entities.size() + " entities in " + time + "ms");
    }

    protected File toFileForceExists(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IOException(filename
                    + " does not exist. This check is to prevent accidental file generation into a wrong path.");
        }
        return file;
    }

    private void generate(Template template, File outDirFile, String javaPackage, String javaClassName, Schema schema,
                          Entity entity) throws Exception {
        generate(template, outDirFile, javaPackage, javaClassName, schema, entity, null);
    }

    private void generate(Template template, File outDirFile, String javaPackage, String javaClassName, Schema schema,
                          Entity entity, Map<String, Object> additionalObjectsForTemplate) throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("schema", schema);
        root.put("entity", entity);
        if (additionalObjectsForTemplate != null) {
            root.putAll(additionalObjectsForTemplate);
        }
        try {
            File file = toJavaFilename(outDirFile, javaPackage, javaClassName);
            file.getParentFile().mkdirs();

            if (entity != null && entity.getHasKeepSections()) {
                checkKeepSections(file, root);
            }

            Writer writer = new FileWriter(file);
            try {
                template.process(root, writer);
                writer.flush();
                System.out.println("Written " + file.getCanonicalPath());
            } finally {
                writer.close();
            }
        } catch (Exception ex) {
            System.err.println("Data map for template: " + root);
            System.err.println("Error while generating " + javaPackage + "." + javaClassName + " ("
                    + outDirFile.getCanonicalPath() + ")");
            throw ex;
        }
    }

    private void checkKeepSections(File file, Map<String, Object> root) {
        if (file.exists()) {
            try {
                String contents = new String(DaoUtil.readAllBytes(file));

                Matcher matcher;

                matcher = patternKeepIncludes.matcher(contents);
                if (matcher.matches()) {
                    root.put("keepIncludes", matcher.group(1));
                }

                matcher = patternKeepFields.matcher(contents);
                if (matcher.matches()) {
                    root.put("keepFields", matcher.group(1));
                }

                matcher = patternKeepMethods.matcher(contents);
                if (matcher.matches()) {
                    root.put("keepMethods", matcher.group(1));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected File toJavaFilename(File outDirFile, String javaPackage, String javaClassName) {
        String packageSubPath = javaPackage.replace('.', '/');
        File packagePath = new File(outDirFile, packageSubPath);
        File file = new File(packagePath, javaClassName + ".java");
        return file;
    }

}

package com.dazz.dao;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.NotNull;
import com.dazz.dao.annotation.Property;
import com.dazz.dao.annotation.ToMany;
import com.dazz.dao.annotation.ToOne;
import com.dazz.dao.annotation.Unique;
import com.google.auto.service.AutoService;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import de.greenrobot.daogenerator.Schema;

/**
 * @author yzw
 */

@AutoService(Processor.class)
public class GreenDaoExtension extends AbstractProcessor {

    Elements elementUtils;
    ProcessingEnvironment env;

    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.env = env;
        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return DaoAnnotationConfig.SUPPORT_ANNOTATION_SET;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Map<String, Schema> mSchemaMap = new HashMap<>();
        Map<Schema, Map<String, de.greenrobot.daogenerator.Entity>> mEntityMap = new HashMap<>();
        Map<de.greenrobot.daogenerator.Entity, Map<String, String>>
                // <Entity,Map<fkey,fClassName>>
                toOneMap = new HashMap<>();
        Map<String, String> fkeyClassMap = new HashMap<>();

        initSchema(roundEnv, mSchemaMap);

        initEntry(roundEnv, mSchemaMap, mEntityMap, toOneMap, fkeyClassMap);

        for (Map.Entry<de.greenrobot.daogenerator.Entity, Map<String, String>> entityToOneEntry : toOneMap.entrySet()) {
            de.greenrobot.daogenerator.Entity entity = entityToOneEntry.getKey();
            Map<String, String> entityToOne = entityToOneEntry.getValue();

            for (Map.Entry<String, String> toOne : entityToOne.entrySet()) {
                String fkey = toOne.getKey();
                String fkeyClassType = fkeyClassMap.get(fkey);
                String fClass = toOne.getValue();

                entity.addLongProperty(fkey);
                env.getMessager().printMessage(Diagnostic.Kind.NOTE, "----------> add to one config fkey: " + fkey + " class name: " + fClass);
                entity.addToOneConfig(new ToOneRelation(entity, fkey, fkeyClassType, fClass));
            }

        }

        generate(mSchemaMap);

        return true;
    }

    private void generate(Map<String, Schema> mSchemaMap) {
        try {
            DaoGeneratorHelper helper = new DaoGeneratorHelper();
            Collection<Schema> schemas = mSchemaMap.values();
            for (Schema schema : schemas) {
                helper.generateAll(schema, schema.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("DaoGeneratorHelper exception!");
        }
    }

    private void initEntry(RoundEnvironment roundEnv, Map<String, Schema> mSchemaMap, Map<Schema, Map<String, de.greenrobot.daogenerator.Entity>> mEntityMap, Map<de.greenrobot.daogenerator.Entity, Map<String, String>> toOneMap, Map<String, String> fkeyClassMap) {
        Set<? extends Element> entitySet = roundEnv.getElementsAnnotatedWith(Entity.class);
        for (Element e : entitySet) {

            String elementPackagePath = e.asType().toString();
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, elementPackagePath);

            // TODO: 2017/1/11 0011 how to know the e is class or interface?
            Entity entitya = e.getAnnotation(Entity.class);
            String schemaName = entitya.schema();

            Schema schema = mSchemaMap.get(schemaName);
            if (schema == null) {
                throw new IllegalArgumentException("There is no define schema: " + schemaName + ". Please check!");
            }

            Map<String, de.greenrobot.daogenerator.Entity> entityMap = mEntityMap.get(schema);
            if (entityMap == null) {
                entityMap = new HashMap<>();
                mEntityMap.put(schema, entityMap);
            }

            String entityClassName = e.getSimpleName().toString();
            String nameInDb = entitya.nameInDb();
            if (nameInDb == null || nameInDb.equals(""))
                nameInDb = entityClassName;

            de.greenrobot.daogenerator.Entity entity = entityMap.get(nameInDb);
            if (entity == null) {
                entity = schema.addEntity(nameInDb);
                entity.setClassPackagePath(elementPackagePath);
                entityMap.put(nameInDb, entity);
            }
            entity.setSkipGeneration(true);

            String log = "Entity -> class:" + entityClassName + " schema: " + schemaName + ", nameInDb: " + nameInDb;
            env.getMessager().printMessage(Diagnostic.Kind.NOTE, log);

            if (!(e instanceof TypeElement)) {
                // TODO: 2017/1/11 0011 log
                continue;
            }


            // TODO: 2017/1/12 0012 待扩展index
//            Index[] indexes = entitya.indexes();


            List<? extends Element> enclosedElementSet = e.getEnclosedElements();
            if (enclosedElementSet == null) continue;

            for (Element el : enclosedElementSet) {
                // care other element in super or object,where is id == null and so on

                Property property = el.getAnnotation(Property.class);
                if (property != null) {

//                    String info = el.getEnclosingElement().getSimpleName().toString();
//                    // Person
//                    env.getMessager().printMessage(Diagnostic.Kind.NOTE, "--------------->" + info);
//                    // java.lang.String or int
//                    env.getMessager().printMessage(Diagnostic.Kind.NOTE, "--------------->" + el.asType().toString());
//                    // DECLARED or INT
//                    env.getMessager().printMessage(Diagnostic.Kind.NOTE, "--------------->" + el.asType().getKind().name());


                    String propertyNameInDb = property.nameInDb();
                    if (propertyNameInDb == null || propertyNameInDb.equals(""))
                        propertyNameInDb = el.getSimpleName().toString();

                    de.greenrobot.daogenerator.Property.PropertyBuilder propertyBuilder = null;


                    String logInfo = "Property Name: " + propertyNameInDb + " type: " + el.asType().toString();
                    env.getMessager().printMessage(Diagnostic.Kind.NOTE, "--------------->" + logInfo);

                    switch (el.asType().getKind()) {
                        case DECLARED:
                            String declared = el.asType().toString();
                            if (declared.equals(String.class.getName())) {
                                propertyBuilder = entity.addStringProperty(propertyNameInDb);
                            } else if (declared.contains(List.class.getName())) {
//                                throw new IllegalArgumentException("please use @ToOne to support List");
                            } else if (declared.contains(Map.class.getName())) {
//                                throw new IllegalArgumentException("please use @ToMany to support Map");
                            }
                            break;
                        case INT:
                            propertyBuilder = entity.addIntProperty(propertyNameInDb);
                            break;
                        case BOOLEAN:
                            propertyBuilder = entity.addBooleanProperty(propertyNameInDb);
                            break;
                        case SHORT:
                            propertyBuilder = entity.addShortProperty(propertyNameInDb);
                            break;
                        case BYTE:
                            propertyBuilder = entity.addByteProperty(propertyNameInDb);
                            break;
                        case LONG:
                            propertyBuilder = entity.addLongProperty(propertyNameInDb);
                            break;
                        case FLOAT:
                            propertyBuilder = entity.addFloatProperty(propertyNameInDb);
                            break;
                        case DOUBLE:
                            propertyBuilder = entity.addDoubleProperty(propertyNameInDb);
                            break;
                    }

                    ToOne toOne = el.getAnnotation(ToOne.class);
                    if (toOne != null) {
                        String fkey = toOne.foreignKey();
                        String fkeyType = toOne.foreignKeyType();
                        if (!fkeyType.toUpperCase().equals("LONG") && !fkeyType.equals("String"))
                            throw new IllegalArgumentException("ToOne foreign key type is error, only accept long or String, but " + fkeyType);
                        String className = el.asType().toString();
                        env.getMessager().printMessage(Diagnostic.Kind.NOTE, "////// ToOne: fkey: " + fkey + " fkeyType: " + fkeyType + " className: " + className);

                        Map<String, String> entityToOneMap = toOneMap.get(entity);
                        if (entityToOneMap == null) {
                            entityToOneMap = new HashMap<>();
                            toOneMap.put(entity, entityToOneMap);
                        }
                        entityToOneMap.put(fkey, className);

                        fkeyClassMap.put(fkey, fkeyType);

                        // ToOne don't support id....,
                        // TODO: 2017/1/14 0014 check if has id
                        continue;
                    }

                    ToMany toMany = el.getAnnotation(ToMany.class);
                    if (toMany != null) {
                        String rfkey = toMany.referencedfKey();
                        String rkey = toMany.referencedKey();
                        String toManyClassType = el.asType().toString();

                        if (!toManyClassType.startsWith("java.util.List") && !toManyClassType.startsWith("java.util.ArrayList"))
                            throw new IllegalArgumentException("ToMany only support List or ArrayList, please check!");

                        int index = toManyClassType.indexOf("<");
                        int lastIndex = toManyClassType.lastIndexOf(">");
                        String toClassType = null;
                        if (index != -1)
                            toClassType = toManyClassType.substring(index + 1, lastIndex);
                        env.getMessager().printMessage(Diagnostic.Kind.NOTE, "////////// " +
                                "ToMany fkey " + rfkey + " toManyClassType: " + toManyClassType + " toClassType: " + toClassType);

                        ToManyRelation toManyRelation = new ToManyRelation(rkey,toClassType);
                        entity.addToManyRelationList(toManyRelation);

                        // ToMany don't support id....,
                        // TODO: 2017/1/14 0014 check if has id
                        continue;
                    }

                    if (propertyBuilder == null) {
                        throw new IllegalArgumentException("add property exception!!! please check you property");
                    }

                    Id id = el.getAnnotation(Id.class);
                    if (id != null) {
                        boolean autoIncrement = id.autoincrement();
                        if (!autoIncrement)
                            propertyBuilder.primaryKey();
                        else
                            propertyBuilder.primaryKey().autoincrement();

                        // ======================================================= log
                        String idName = el.getSimpleName().toString();
                        String typeKind = el.getKind().toString();
                        log = "Id -> type kind :" + typeKind + " idName: " + idName + " auto Increment: " + autoIncrement;
                        env.getMessager().printMessage(Diagnostic.Kind.NOTE, log);
                    }

                    NotNull notnull = el.getAnnotation(NotNull.class);
                    if (notnull != null) {
                        propertyBuilder.notNull();
                    }

                    Unique unique = el.getAnnotation(Unique.class);
                    if (unique != null)
                        propertyBuilder.unique();


                    // 没有扩展OrderBy


                }
            }
        }
    }

    private void initSchema(RoundEnvironment roundEnv, Map<String, Schema> mSchemaMap) {
        Set<? extends Element> schemaAnnotationSet = roundEnv.getElementsAnnotatedWith(com.dazz.dao.annotation.Schema.class);
        for (Element e : schemaAnnotationSet) {
            com.dazz.dao.annotation.Schema schemaa = e.getAnnotation(com.dazz.dao.annotation.Schema.class);
            String path = schemaa.path();
            path = path.replace('.', File.separatorChar);
            String packageName = schemaa.packageName();
            int version = schemaa.version();
            String schemaName = schemaa.name();

            String log = "--------> Schema: " + schemaName + " path: " + path + " packageName: " + packageName + " version: " + version;
            env.getMessager().printMessage(Diagnostic.Kind.WARNING, log);

            Schema schema = mSchemaMap.get(schemaName);
            if (schema == null) {
                schema = new Schema(version, schemaName, path, packageName);
                mSchemaMap.put(schemaName, schema);
            }
        }
    }
}

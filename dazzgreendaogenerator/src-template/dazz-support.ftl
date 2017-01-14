package ${entity.javaPackage};

import java.util.List;
import com.yzw.dazzgreendao.AppDaoManager;
<#list entity.toOneConfigList as item>
import ${item.fClass};
</#list>

public abstract class ${schema.schemaName}${entity.className} {

    <#list entity.toOneConfigList as item>
    protected abstract ${item.fkeyClass} toOne${item.fkey?cap_first}();

    public ${item.fClassName} get${item.fClassName}() {
       List<${item.fClass}> list = AppDaoManager.get("${schema.schemaName}")
            .query(${item.fClassName}.class)
            .where(${item.fClassName}Dao.Properties.${item.fkey?cap_first}.eq(toOne${item.fkey?cap_first}()))
            .limit(1).list();

       if (list == null || list.size() == 0)  return null;
       else return list.get(0);
    }

    </#list>
}
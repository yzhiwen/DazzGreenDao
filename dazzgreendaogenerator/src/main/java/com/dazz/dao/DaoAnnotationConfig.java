package com.dazz.dao;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Index;
import com.dazz.dao.annotation.NotNull;
import com.dazz.dao.annotation.OrderBy;
import com.dazz.dao.annotation.Schema;
import com.dazz.dao.annotation.ToMany;
import com.dazz.dao.annotation.ToOne;
import com.dazz.dao.annotation.Transient;
import com.dazz.dao.annotation.Unique;

import java.util.HashSet;
import java.util.Set;

/**
 * 注解配置
 *
 * @author yzw
 */

public class DaoAnnotationConfig {

    public static Set<String> SUPPORT_ANNOTATION_SET = new HashSet<>();

    static {
        SUPPORT_ANNOTATION_SET.add(Schema.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(Entity.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(Id.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(NotNull.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(Index.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(OrderBy.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(ToOne.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(ToMany.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(Transient.class.getCanonicalName());
        SUPPORT_ANNOTATION_SET.add(Unique.class.getCanonicalName());
    }

}

package com.dazz.dao.annotation;

/**
 * @author yzw
 */

public @interface Schema {


    /**
     * Schema Name
     *
     * @return
     */
    String name();

    /**
     * Schema Version
     *
     * @return
     */
    int version();

    /**
     * 生成Schema相应Dao文件路径
     * eg: app.src.main.java
     *
     * @return
     */
    String path();

    /**
     * schema包名
     * eg: com.xxx.xxx
     *
     * @return
     */
    String packageName();


}

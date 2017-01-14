package com.dazz.dao;

import de.greenrobot.daogenerator.Entity;

/**
 * @author yzw
 */

public class ToOneConfig {

    private Entity entity;
    private String fkey;
    private String fkeyClass;
    private String fkeyClassName;
    private String fClass;
    private String fClassName;

    public ToOneConfig(Entity entity, String fkey, String fkeyClass, String fClass) {
        this.entity = entity;
        this.fkey = fkey;
        this.fkeyClass = fkeyClass;
        this.fClass = fClass;
        this.fClassName = fClass;
        int index = fClass.lastIndexOf('.');
        if (index != -1)
            this.fClassName = fClass.substring(index + 1);
    }


    public String getFkeyClass() {
        return fkeyClass;
    }

    public void setFkeyClass(String fkeyClass) {
        this.fkeyClass = fkeyClass;
    }

    public String getFkeyClassName() {
        return fkeyClassName;
    }

    public void setFkeyClassName(String fkeyClassName) {
        this.fkeyClassName = fkeyClassName;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public String getfClass() {
        return fClass;
    }

    public void setfClass(String fClass) {
        this.fClass = fClass;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public String getfClassName() {
        return fClassName;
    }

    public void setfClassName(String fClassName) {
        this.fClassName = fClassName;
    }
}

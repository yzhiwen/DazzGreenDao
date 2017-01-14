package com.dazz.dao;

/**
 * @author yzw
 */

public class ToManyRelation {

    private String rKey;// to relation key
    private String rClass; // to relation Class string xxx.xxx.xxxBean
    private String rClassName; // xxxBean

    public ToManyRelation(String rKey, String rClass) {
        this.rKey = rKey;
        this.rClass = rClass;
        this.rClassName = rClass;
        int index = rClass.lastIndexOf('.');
        if (index != -1)
            this.rClassName = rClass.substring(index + 1);
    }

    public String getrKey() {
        return rKey;
    }

    public void setrKey(String rKey) {
        this.rKey = rKey;
    }

    public String getrClass() {
        return rClass;
    }

    public void setrClass(String rClass) {
        this.rClass = rClass;
    }

    public String getrClassName() {
        return rClassName;
    }

    public void setrClassName(String rClassName) {
        this.rClassName = rClassName;
    }
}

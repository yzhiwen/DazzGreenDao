package com.yzw.dazzgreendao;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库管理者，根据数据库名管理数据库，避免数据库过多导致代码混乱
 * Created by yzw on 2016/7/12 0012.
 */
public class AppDaoManager {
    private volatile ConcurrentHashMap<String,DaoManager> daoManagerMap;

    private AppDaoManager(){
        this.daoManagerMap = new ConcurrentHashMap<>();
    }
    private static class AppDaoManagerBuilder{
        public static AppDaoManager appDaoManager = new AppDaoManager();
    }

    public static AppDaoManager getInstance(){
        return AppDaoManagerBuilder.appDaoManager;
    }

    // TODO: 2016/7/12 0012 并发问题
    public static void put(String dbName, DaoManager daoManager) {
        getInstance().daoManagerMap.put(dbName,daoManager);
    }

    public static DaoManager get(String dbName){
        return getInstance().daoManagerMap.get(dbName);
    }
}

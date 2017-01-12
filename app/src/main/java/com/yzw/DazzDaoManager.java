package com.yzw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dazz.dao.annotation.Schema;
import com.yzw.dazzdao.DazzDaoMaster;
import com.yzw.dazzgreendao.DaoManager;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;

/**
 * Created by yzw on 2017/1/12 0012.
 */

@Schema(name = DazzDaoManager.SCHEMA_NAME,
        version = DazzDaoManager.SCHEMA_VERSION,
        path = DazzDaoManager.SCHEMA_PATH,
        packageName = DazzDaoManager.SCHEMA_PACKAGE_NAME)
public class DazzDaoManager extends DaoManager {

    public final static int SCHEMA_VERSION = 1;
    public final static String SCHEMA_PACKAGE_NAME = "com.yzw.dazzdao";
    public final static String SCHEMA_PATH = "app.src.main.java";
    public final static String SCHEMA_NAME = "Dazz";

    public DazzDaoManager(Context context, String dbName) {
        super(context, dbName);
    }


    @Override
    protected AbstractDaoMaster initDaoMaster(SQLiteDatabase writableDb) {
        return new DazzDaoMaster(writableDb);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DazzDaoMaster.createAllTables(db, true);
    }

    @Override
    public void onUpgrade(AbstractDaoSession daoSession, int mOldVersion, int mNewVersion) {

    }
}

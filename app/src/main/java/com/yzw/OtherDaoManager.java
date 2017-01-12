package com.yzw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dazz.dao.annotation.Schema;
import com.yzw.dazzgreendao.DaoManager;
import com.yzw.otherdao.OtherDaoMaster;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;

/**
 * Created by yzw on 2017/1/12 0012.
 */

@Schema(name = OtherDaoManager.SCHEMA_NAME,
        version = OtherDaoManager.SCHEMA_VERSION,
        path = OtherDaoManager.SCHEMA_PATH,
        packageName = OtherDaoManager.SCHEMA_PACKAGE_NAME)
public class OtherDaoManager extends DaoManager {
    public final static int SCHEMA_VERSION = 1;
    public final static String SCHEMA_PACKAGE_NAME = "com.yzw.otherdao";
    public final static String SCHEMA_PATH = "app.src.main.java";
    public final static String SCHEMA_NAME = "Other";

    public OtherDaoManager(Context context, String dbName) {
        super(context, dbName);
    }

    @Override
    protected AbstractDaoMaster initDaoMaster(SQLiteDatabase writableDb) {
        return new OtherDaoMaster(writableDb);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        OtherDaoMaster.createAllTables(db,true);
    }

    @Override
    public void onUpgrade(AbstractDaoSession daoSession, int mOldVersion, int mNewVersion) {

    }
}

package com.yzw.dazzgreendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import de.greenrobot.dao.AbstractDaoSession;

/**
 * 构建数据库的配置，解决GreenDao通过SQLiteOpenHelper创造数据库的限制：
 * 之前创造过程：onCreate -> onUpdate -> new Daomaster -> new DaoSesion
 * 现在创造过程：onCreate -> new Daomaster -> new DaoSession -> onUpdate
 * Created by yzw on 2016/7/27 0027.
 */
public class DaoConfig {

    private Context mContext;
    private String mDataBaseName;
    private OnSQLiteDatabaseListener mSqLiteDatabaseListener;

    public DaoConfig(Context mContext, String mDataBaseName, OnSQLiteDatabaseListener mSqLiteDatabaseListener) {
        this.mContext = mContext;
        this.mDataBaseName = mDataBaseName;
        this.mSqLiteDatabaseListener = mSqLiteDatabaseListener;
    }

    public Context getmContext() {
        return mContext;
    }

    public SQLiteDatabase openOrCreateDatabase() {
        SQLiteDatabase db = mContext.openOrCreateDatabase(mDataBaseName, Context.MODE_PRIVATE, null);
        if (db.getVersion() == 0)
            mSqLiteDatabaseListener.onCreate(db);
        return db;
    }

    public void checkUpdate(AbstractDaoSession daoSession, int mOldVersion, int mNewVersion) {
        Log.e("DAO CONFIG", "checkUpdate oldVersion " + mOldVersion + " newVersion " + mNewVersion);
        if (mOldVersion < mNewVersion) {
            mSqLiteDatabaseListener.onUpgrade(daoSession, mOldVersion, mNewVersion);
            daoSession.getDatabase().setVersion(mNewVersion);
        }
    }
}

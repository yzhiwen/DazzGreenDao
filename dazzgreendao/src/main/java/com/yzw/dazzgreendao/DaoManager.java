package com.yzw.dazzgreendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.query.QueryBuilder;


/**
 * 基于GreenDao生成的...DaoMaster（相当与一个数据库）数据接口的抽象</br> 须实现下列步骤：
 * 1）.根据生成的...DaoMaster，在initHelper()中创建DaoHelper，并实现DaoHelper的创表工作，即直接调用相应 DaoMaster.createAllTables(db, false);
 * 2）.根据生成的...DaoMaster，创建对象
 * Created by yzw on 2016/7/11 0011.
 */
public abstract class DaoManager implements DaoService, OnSQLiteDatabaseListener {

    private String dbName;
    private Context context;

    private OnSQLiteDatabaseListener mSqLiteDatabaseListener;
    private AbstractDaoMaster daoMaster;
    private DaoConfig mDaoConfig;
    private AbstractDaoSession daoSession;

    public DaoManager(Context context, String dbName) {
        this.mSqLiteDatabaseListener = this;
        this.context = context.getApplicationContext();
        this.dbName = dbName;
        init();
        AppDaoManager.getInstance().put(dbName, this);
    }

    public AbstractDaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(AbstractDaoSession daoSession) {
        this.daoSession = daoSession;
    }

    private void init() {
        mDaoConfig = new DaoConfig(this.context, this.dbName, mSqLiteDatabaseListener);
        this.daoMaster = initDaoMaster(mDaoConfig.openOrCreateDatabase());
        this.daoSession = daoMaster.newSession();
        mDaoConfig.checkUpdate(daoSession, daoMaster.getDatabase().getVersion(), daoMaster.getSchemaVersion());
    }

    /**
     * 创建相应new DaoMaster(daoHelper.getWritableDb());
     *
     * @param writableDb
     * @return
     */
    protected abstract AbstractDaoMaster initDaoMaster(SQLiteDatabase writableDb);

    @Override
    public <T> void insert(T t) {
        daoSession.insertOrReplace(t);
    }

    @Override
    public <T> void insert(List<T> list) {
        for (T t : list) {
            insert(t);
        }
    }

    @Override
    public <T> void update(T t) {
        daoSession.update(t);
    }

    @Override
    public <T> void delete(T t) {
        daoSession.delete(t);
    }

    @Override
    public <T> void deleteAll(Class<T> t) {
        daoSession.deleteAll(t);
    }

    @Override
    public <T> List<T> loadAll(Class<T> t) {
        return daoSession.loadAll(t);
    }

    @Override
    public <T> QueryBuilder<T> query(Class<T> t) {
        return (QueryBuilder<T>) daoSession.getDao(t).queryBuilder();
    }
}

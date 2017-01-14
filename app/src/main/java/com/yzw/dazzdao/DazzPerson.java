package com.yzw.dazzdao;

import java.util.List;
import com.yzw.dazzgreendao.AppDaoManager;
import com.yzw.PersonInfo;

public abstract class DazzPerson {

    protected abstract long toOnePersonInfoId();

    public PersonInfo getPersonInfo() {
       List<com.yzw.PersonInfo> list = AppDaoManager.get("Dazz")
            .query(PersonInfo.class)
            .where(PersonInfoDao.Properties.PersonInfoId.eq(toOnePersonInfoId()))
            .limit(1).list();

       if (list == null || list.size() == 0)  return null;
       else return list.get(0);
    }

}
package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;

/**
 * Created by yzw on 2017/1/14 0014.
 */

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class PersonInfo {

    @Id
    @Property
    private long personInfoId;

    @Property
    private String personInfo;


    public PersonInfo(long personInfoId, String personInfo) {
        this.personInfoId = personInfoId;
        this.personInfo = personInfo;
    }

    @Override
    public String toString() {
        return "PersonInfo{" +
                "personId=" + personInfoId +
                ", personInfo='" + personInfo + '\'' +
                '}';
    }

    public long getPersonInfoId() {
        return personInfoId;
    }

    public void setPersonInfoId(long personId) {
        this.personInfoId = personId;
    }

    public String getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(String personInfo) {
        this.personInfo = personInfo;
    }
}

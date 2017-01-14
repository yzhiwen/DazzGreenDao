package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;
import com.dazz.dao.annotation.ToOne;
import com.yzw.dazzdao.DazzPerson;

/**
 * Created by yzw on 2017/1/11 0011.
 */

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class Person extends DazzPerson {

    @Id
    @Property
    private long id;

    //    @Id
    @Property
    private String name;

    //    @Id
    @Property
    private int age;

    @ToOne(foreignKey = "personInfoId",foreignKeyType = "long")
    @Property
    private PersonInfo personInfo;

    private long personInfoId;

    /////////////////////////////////////////////////////////////
    // 下面这些是必须提供
    /////////////////////////////////////////////////////////////

    public Person(long id, String name, int age, long personInfoId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.personInfoId = personInfoId;
    }

    public long getPersonInfoId() {
        return personInfoId;
    }

    public void setPersonInfoId(long personInfoId) {
        this.personInfoId = personInfoId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", personInfoId=" + personInfoId +
                '}';
    }

    @Override
    protected long toOnePersonInfoId() {
        return personInfoId;
    }
}

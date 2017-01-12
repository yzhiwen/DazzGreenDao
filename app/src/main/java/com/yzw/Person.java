package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;

/**
 * Created by yzw on 2017/1/11 0011.
 */

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class Person {

    @Id
    @Property
    private long id;

//    @Id
//    @Property
    private String name;

//    @Id
//    @Property
    private int age;

    /////////////////////////////////////////////////////////////
    // 下面这些是必须提供
    /////////////////////////////////////////////////////////////


    public Person(long id) {
        this.id = id;
    }

    public Person(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
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
                '}';
    }
}

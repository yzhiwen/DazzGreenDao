package com.yzw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yzw.dazzgreendao.AppDaoManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDaoManager.put(OtherDaoManager.SCHEMA_NAME, new OtherDaoManager(this, OtherDaoManager.SCHEMA_NAME));
        long count = AppDaoManager.get(OtherDaoManager.SCHEMA_NAME)
                .query(OtherInfo.class).count();

        AppDaoManager.get(OtherDaoManager.SCHEMA_NAME)
                .insert(new OtherInfo((count + 1) + "", true, "hhhh"+(count+1)));

        List<OtherInfo> list1 = AppDaoManager.get(OtherDaoManager.SCHEMA_NAME)
                .query(OtherInfo.class)
                .build().list();

        for (OtherInfo info : list1)
            System.out.println(info.toString());


        AppDaoManager.put(DazzDaoManager.SCHEMA_NAME, new DazzDaoManager(this, DazzDaoManager.SCHEMA_NAME));
        count = AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .query(PersonInfo.class).count();
        AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .insert(new PersonInfo(count + 132, "person info for 132"));

        long personCount = AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .query(PersonInfo.class).count();
        AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .insert(new Person(personCount + 110, "zw", 12, count + 132));

        List<Person> list = AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .query(Person.class).list();

        for (Person p : list) {
            System.out.println(p.toString());
            System.out.println(p.getPersonInfo().toString());
        }

    }
}

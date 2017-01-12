package com.yzw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yzw.dazzgreendao.AppDaoManager;
import com.yzw.dazzgreendao.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDaoManager.put(DazzDaoManager.SCHEMA_NAME, new DazzDaoManager(this, DazzDaoManager.SCHEMA_NAME));

        AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .insert(new Person(110, "zw", 12));

        List<Person> list = AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
                .query(Person.class).list();

        for (Person p : list)
            System.out.println(p.toString());

        AppDaoManager.put(OtherDaoManager.SCHEMA_NAME, new OtherDaoManager(this, OtherDaoManager.SCHEMA_NAME));
        AppDaoManager.get(OtherDaoManager.SCHEMA_NAME)
                .insert(new OtherInfo("1", true, "hhhh"));

        List<OtherInfo> list1 = AppDaoManager.get(OtherDaoManager.SCHEMA_NAME)
                .query(OtherInfo.class)
                .build().list();

        for (OtherInfo info : list1)
            System.out.println(info.toString());

    }
}

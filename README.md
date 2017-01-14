# DazzGreenDao
主要修改GreenDao的Generator操作，通过注解实体类，实现多Schema和Master和Session的生成

# Usage

##Basic

###Bean
```java

@Entity(schema = OtherDaoManager.SCHEMA_NAME)
public class OtherInfo {
    @Id // 主键只能是long或String类型
    @Property
    private String id;

    @Property
    private boolean isOk;

    @Property
//    @Id
    private String info;


    public OtherInfo(String id, boolean isOk, String info) {
        this.id = id;
        this.isOk = isOk;
        this.info = info;
    }

    // get and set
}

```

###Dao Manager Config
```java
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
```

###Use
```java
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
```

##ToOne

###Bean
```java

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

    // get and set
}


@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class PersonInfo {

    @Id
    @Property
    private long personInfoId;

    @Property
    private String personInfo;

    // get and set
}
```

###Manager Config

```java
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
```

### Use

```java
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
```
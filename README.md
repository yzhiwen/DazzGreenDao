# DazzGreenDao
主要修改GreenDao的Generator操作，通过注解实体类，实现多Schema和Master和Session的生成

# Usage

###Bean
```java
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

    //......
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

### use

```java
AppDaoManager.put(DazzDaoManager.SCHEMA_NAME, new DazzDaoManager(this, DazzDaoManager.SCHEMA_NAME));

AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
        .insert(new Person(110, "zw", 12));

List<Person> list = AppDaoManager.get(DazzDaoManager.SCHEMA_NAME)
        .query(Person.class).list();

for (Person p : list)
    System.out.println(p.toString());

```
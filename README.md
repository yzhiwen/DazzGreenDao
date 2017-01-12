# DazzGreenDao
主要修改GreenDao的Generator操作，通过注解实体类，实现多Schema和Master和Session的生成

# Usage

```java

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class Person {

    @Id
    @Property
    private long id;
/*
    @Property
    @ToOne(joinProperty = "PersonInfo")
    private long personInfoId;*/

//    @Id
//    @Property
    private String name;

//    @Id
//    @Property
    private int age;

   ......
}
```

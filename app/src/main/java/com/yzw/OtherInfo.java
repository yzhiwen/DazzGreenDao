package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;

/**
 * Created by yzw on 2017/1/12 0012.
 */
@Entity(schema = OtherDaoManager.SCHEMA_NAME)
public class OtherInfo {
    @Id // 主键只能是long或String类型
    @Property
    private String id;

    @Property
    private boolean isOk;

    @Property
    @Id
    private String info;


    public OtherInfo(String id, boolean isOk, String info) {
        this.id = id;
        this.isOk = isOk;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    ///////////////////////////////////// as 会自动生成isOk 和ok。。。。
    public boolean getIsOk() {
        return isOk;
    }

    public void setIsOk(boolean ok) {
        isOk = ok;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "OtherInfo{" +
                "id=" + id +
                ", isOk=" + isOk +
                ", info='" + info + '\'' +
                '}';
    }
}

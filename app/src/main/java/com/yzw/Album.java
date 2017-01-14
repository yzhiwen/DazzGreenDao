package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;
import com.dazz.dao.annotation.Schema;
import com.dazz.dao.annotation.ToMany;
import com.yzw.dazzdao.SongDao;
import com.yzw.dazzgreendao.AppDaoManager;

import java.util.List;

/**
 * Created by yzw on 2017/1/14 0014.
 */

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class Album {

    @Id
    @Property
    private long albumId;

    @Property
    private String info;

    // referencedJoinProperty是在Song的键，referencedKey是存在AlbumId的键
    @ToMany(referencedfKey = "songId", referencedKey = "albumId")
    @Property
    private List<Song> songList;



    public Album(long albumId, String info) {
        this.albumId = albumId;
        this.info = info;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", info='" + info + '\'' +
                ", songList=" + songList +
                '}';
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

package com.yzw;

import com.dazz.dao.annotation.Entity;
import com.dazz.dao.annotation.Id;
import com.dazz.dao.annotation.Property;
import com.dazz.dao.annotation.ToOne;

/**
 * Created by yzw on 2017/1/14 0014.
 */

@Entity(schema = DazzDaoManager.SCHEMA_NAME)
public class Song {

    @Id
    @Property
    private long songId;

    @Property
    private String songText;

    @ToOne(foreignKey = "albumId", foreignKeyType = "long")
    @Property
    private Album album;

    private long albumId;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Song(long songId, String songText, long albumId) {
        this.songId = songId;
        this.songText = songText;
        this.albumId = albumId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", songText='" + songText + '\'' +
                '}';
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getSongText() {
        return songText;
    }

    public void setSongText(String songText) {
        this.songText = songText;
    }
}

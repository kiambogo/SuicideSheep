package kiambogo.suicidesheep.models;

import java.util.Date;

/**
 * Created by christopher on 31/01/15.
 */
public class Song {
    Integer id;
    String name;
    String artist;
    Integer duration;
    Date uploadDate;
    Integer viewCount;
    Boolean isMix;
    Boolean isSheeep;

    public Song(Integer id, String name, String artist, Integer duration, Date uploadDate, Integer viewCount, Boolean isMix, Boolean isSheeep) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.uploadDate = uploadDate;
        this.viewCount = viewCount;
        this.isMix = isMix;
        this.isSheeep = isSheeep;
    }

    public Integer getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getArtist() {
        return this.artist;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Integer getViewCount() {
        return this.viewCount;
    }

    public Date getUploadDate() {
        return this.uploadDate;
    }

    public Boolean getIsMix() { return this.isMix; }

    public Boolean getIsSheeep() { return this.isSheeep; }

}

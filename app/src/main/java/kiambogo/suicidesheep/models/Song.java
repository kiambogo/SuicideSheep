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
    Integer viewCount;
    Date uploadDate;

    public Song(Integer id, String name, String artist, Integer duration, Integer viewCount, Date uploadDate) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.viewCount = viewCount;
        this.uploadDate = uploadDate;
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
}

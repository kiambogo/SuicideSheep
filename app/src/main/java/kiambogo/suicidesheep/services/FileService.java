package kiambogo.suicidesheep.services;

import android.content.Context;

import java.io.File;

import kiambogo.suicidesheep.models.Song;

/**
 * Created by cpoenaru on 2/3/15.
 */
public class FileService {
    Context context;

    public FileService(Context context) {
        this.context = context;
    }

    public boolean isSongDownloaded(Integer songID) {
        File songFile = new File(context.getFilesDir().getAbsolutePath() + "/songs/" + songID);
        return songFile.exists();
    }
}

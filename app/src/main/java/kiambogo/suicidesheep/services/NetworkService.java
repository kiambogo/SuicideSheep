package kiambogo.suicidesheep.services;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

import kiambogo.suicidesheep.models.Song;

/**
 * Created by christopher on 02/02/15.
 */
public class NetworkService {

    Context context = null;

    public NetworkService(Context context) {
        this.context = context;
    }

    public void downloadDatabase() throws IOException {
        URL url = null;
        File db = null;

        try {
            url = new URL("https://s3.amazonaws.com/suicide-sheep/suicidesheep.db");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            db = new File(context.getFilesDir(), "suicidesheep.db");

            OutputStream outputStream = new FileOutputStream(db);

            int read;
            byte[] bytes = new byte[4096];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        finally {
            urlConnection.disconnect();
            System.out.println("Saved database to " + db.getAbsolutePath());
        }

    }

    public void downloadSong(Song song) throws IOException {
        URL url = null;

        try {
            url = new URL("https://s3.amazonaws.com/suicide-sheep/suicidesheep.db");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            File songsDir = new File(context.getFilesDir().getAbsolutePath() + "/songs/");
            if (!songsDir.exists())
                songsDir.mkdirs();
            File songFile = new File(songsDir, song.getID().toString());

            OutputStream outputStream = new FileOutputStream(songFile);

            int read;
            byte[] bytes = new byte[8192];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
        finally {
            urlConnection.disconnect();
            Log.i("Network Service >> Download Song", "Downloaded song "+song.getName());
        }

    }
}

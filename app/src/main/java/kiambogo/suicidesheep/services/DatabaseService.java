package kiambogo.suicidesheep.services;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kiambogo.suicidesheep.models.Song;

/**
 * Created by christopher on 02/02/15.
 */
public class DatabaseService extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/kiambogo.suicidesheep/files/suicidesheep.db";
    private static String DB_NAME = "suicidesheep.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    public DatabaseService(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public List<Song> getAllSongsFromSongs() throws SQLException{
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM songs";

        openDataBase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
//                    parserSDF.parse(cursor.getString(4)),
                            new Date(),
                    cursor.getInt(5),
                    (cursor.getString(6) == "true") ? true:false,
                    (cursor.getString(6) == "false") ? true:false);
//                }
//                catch (ParseException e) {
//                    e.printStackTrace();
//                }

                songList.add(song);
            } while (cursor.moveToNext());
        }
        return songList;
    }

    public List<Song> getSongsWithPage(Integer page) throws SQLException {
        List<Song> songList = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM songs ORDER BY uploadDate DESC LIMIT 20 OFFSET " + page*50;

        openDataBase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        SimpleDateFormat parserSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Song song = null;
                try {
                    song = new Song(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            parserSDF.parse(cursor.getString(4)),
                            cursor.getInt(5),
                            (cursor.getString(6) == "true") ? true:false,
                            (cursor.getString(6) == "false") ? true:false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                songList.add(song);
            } while (cursor.moveToNext());
        }
        myDataBase.close();
        return songList;
    }

    public URL getThumbnailLink(Integer songID) throws MalformedURLException {
        URL url = new URL("https://yt3.ggpht.com/-nkC01SMBUms/AAAAAAAAAAI/AAAAAAAAAAA/X4GElTDAELg/s900-c-k-no/photo.jpg");

        openDataBase();
        String selectQuery = "SELECT thumbnail_url FROM images where _id = "+songID;

        Cursor cursor = myDataBase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                url = new URL(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        myDataBase.close();
        return url;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

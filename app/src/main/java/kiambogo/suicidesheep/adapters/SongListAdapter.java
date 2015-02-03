package kiambogo.suicidesheep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import kiambogo.suicidesheep.R;
import kiambogo.suicidesheep.services.ThumbnailService;
import kiambogo.suicidesheep.models.Song;
import kiambogo.suicidesheep.services.DatabaseService;

/**
 * Created by christopher on 31/01/15.
 */
public class SongListAdapter extends ArrayAdapter<Song> {
    private final Context context;
    private List<Song> songList;

    public SongListAdapter(List<Song> songs, Context context) {
        super(context, R.layout.song_list_item, songs);
        this.context = context;
        this.songList = songs;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Song song = songList.get(position);
        ThumbnailService thumbnailService = new ThumbnailService();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        ImageView picture = (ImageView) convertView.findViewById(R.id.thumbnail);
        ImageView downloaded = (ImageView) convertView.findViewById(R.id.downloaded);

        name.setText(song.getName());
        artist.setText(song.getArtist());
        duration.setText(parseDuration(song.getDuration()));

        try {
            thumbnailService.fetchDrawableOnThread(getThumbURL(song), picture);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        downloaded.setImageResource(R.drawable.download_icon);
        if (isDownloaded(song.getID()))
            downloaded.setVisibility(View.VISIBLE);
        else
            downloaded.setVisibility(View.GONE);

        return convertView;
    }

    private String getThumbURL(Song song) throws MalformedURLException {
        DatabaseService databaseService = new DatabaseService(context);

        return databaseService.getThumbnailLink(song.getID()).toString();
    }

    private String parseDuration(Integer duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        int length = String.valueOf(seconds).length();
        if (length == 1) {
            String realseconds = "0" + seconds;
            return minutes + ":" + realseconds;
        }
        else {
            return minutes + ":" + seconds;
        }
    }

    private Boolean isDownloaded(Integer songID) {
        File songDir = getContext().getDir("songs", Context.MODE_PRIVATE);
        File songFile = new File(songDir.getAbsolutePath() + "/" + songID.toString() + ".mp4");
        return songFile.exists();
    }
}

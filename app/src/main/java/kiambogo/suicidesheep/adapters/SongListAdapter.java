package kiambogo.suicidesheep.adapters;

import android.content.Context;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Console;
import java.util.List;

import kiambogo.suicidesheep.R;
import kiambogo.suicidesheep.models.Song;

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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        TextView duration = (TextView) convertView.findViewById(R.id.duration);
        ImageView picture = (ImageView) convertView.findViewById(R.id.downloaded);
        ImageView downloaded = (ImageView) convertView.findViewById(R.id.downloaded);

        System.out.println(song.getDuration() + ":dur");
        name.setText(song.getName());
        artist.setText(song.getArtist());
        duration.setText(song.getDuration().toString());
        picture.setImageResource(R.drawable.download_icon);
        downloaded.setImageResource(R.drawable.download_icon);

        return convertView;
    }
}

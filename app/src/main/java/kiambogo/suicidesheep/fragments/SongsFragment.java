package kiambogo.suicidesheep.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kiambogo.suicidesheep.R;
import kiambogo.suicidesheep.adapters.SongListAdapter;
import kiambogo.suicidesheep.models.Song;
import kiambogo.suicidesheep.services.DatabaseService;
import kiambogo.suicidesheep.services.FileService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnItemSelectedListener}
 * interface.
 */
public class SongsFragment extends ListFragment {
    private OnItemSelectedListener listener;
    ArrayList<Song> songs = getPage(0);
    Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongsFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new SongListAdapter(songs, getActivity().getApplicationContext()));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        FileService fileService = new FileService(context);
        DatabaseService databaseService = new DatabaseService(context);

//        menu.clearHeader();
        Song selectedSong = songs.get(info.position);
        menu.setHeaderTitle(selectedSong.getName());
        ArrayList<String> arrayMenuItems = new ArrayList<>();

        if (fileService.isSongDownloaded(selectedSong.getID()))
            arrayMenuItems.add("Delete Download");
        else
            arrayMenuItems.add("Download");

        if (!databaseService.isSongInFavourites(selectedSong.getID()))
            arrayMenuItems.add("Add to Favourites");


        String[] menuItems = arrayMenuItems.toArray(new String[arrayMenuItems.size()]);

        for (int i = 0; i<menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getTitle() == "Download") {

        }


        return super.onContextItemSelected(item);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Log.d("SongsFragment", "Item clicked");

        if (listener != null) {
            Log.d("SongsFragment", "Invoking callback...");
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            listener.onItemSelectedListener(songs.get(position).getID());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemSelectedListener {
        // TODO: Update argument type and name
        public void onItemSelectedListener(Integer songID);
    }

    public ArrayList<Song> getPage(Integer page) {
        DatabaseService databaseService = new DatabaseService(context);
        return databaseService.getSongsWithPage(page);
    }

    public ArrayList<Song> getSongs() {
        DatabaseService databaseService = new DatabaseService(context);
        return databaseService.getAllSongs();
    }

}

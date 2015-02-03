package kiambogo.suicidesheep.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kiambogo.suicidesheep.R;
import kiambogo.suicidesheep.controller.MusicController;
import kiambogo.suicidesheep.fragments.SongsFragment;
import kiambogo.suicidesheep.models.Song;
import kiambogo.suicidesheep.services.MediaService;

import android.widget.Button;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.ProgressBar;
import android.widget.SeekBar;


public class MainActivity extends ActionBarActivity
        implements ActionBar.TabListener,
        SongsFragment.OnItemSelectedListener,
        MediaPlayerControl,
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    public ArrayList<Song> songs = new ArrayList();
    private MediaService musicSrv;
    private Intent playIntent;
    private MusicController controller;
    Boolean musicBound = false;
    Button playBtn;
    Button pauseBtn;
    SeekBar seekBar;
    Handler seekHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setController();
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playButton);
        pauseBtn = (Button) findViewById(R.id.pauseButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        SongsFragment songsFragment = new SongsFragment();
        fragmentTransaction.add(R.id.fragmentContainer, songsFragment);
        fragmentTransaction.commit();

        if (playIntent == null) {
            playIntent = new Intent(this, MediaService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }

        seekUpdation();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaService.MusicBinder binder = (MediaService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onItemSelectedListener(Integer id) {
        Log.d("Activity", "Playing song");
        musicSrv.setSong(id);
        musicSrv.playSong();
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        seekBar.setProgress(getCurrentPosition());
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    private void setController(){
        controller = new MusicController(this);

        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
                seekBar.setMax(getDuration());
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
                seekBar.setMax(getDuration());
            }
        });
    }

    //play next
    private void playNext(){
        musicSrv.playNext();
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pauseButton)
            pause();
        else start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    Runnable run = new Runnable() {
        @Override public void run() {
            seekUpdation();
        }
    };

    public void seekUpdation() {
        seekBar.setProgress(getCurrentPosition());
        seekBar.setMax(getDuration());
        seekHandler.postDelayed(run, 1000);
    }

}

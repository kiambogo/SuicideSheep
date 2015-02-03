package kiambogo.suicidesheep.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

import kiambogo.suicidesheep.R;
import kiambogo.suicidesheep.models.Song;
import kiambogo.suicidesheep.services.NetworkService;

/**
 * Created by cpoenaru on 2/2/15.
 */

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT;
    private ProgressBar spinner;
    DownloadDatabase downloadDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            downloadDatabase = new DownloadDatabase();
            downloadDatabase.execute(getApplicationContext());
            SPLASH_TIME_OUT = 10000;
        } else {
            SPLASH_TIME_OUT = 3000;
        }

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private class DownloadDatabase extends AsyncTask<Object, Void, String> {

        NetworkService networkService = new NetworkService(getApplicationContext());

        @Override
        protected String doInBackground(Object... params) {
            try {
                networkService.downloadDatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

        }


    }

}
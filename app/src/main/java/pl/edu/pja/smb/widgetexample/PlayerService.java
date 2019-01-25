package pl.edu.pja.smb.widgetexample;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class PlayerService extends Service {

    public static final String ACTION_START = "pl.edu.pja.smb.widgetexample.action.START";
    public static final String ACTION_PAUSE = "pl.edu.pja.smb.widgetexample.action.PAUSE";
    public static final String ACTION_STOP = "pl.edu.pja.smb.widgetexample.action.STOP";
    public static final String ACTION_SKIP = "pl.edu.pja.smb.widgetexample.action.SKIP";

    MediaPlayer mediaPlayer = null;

    @Override
    public void onCreate() {
        Log.i("PlayerService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Log.i("PlayerService", "OnStartCommand:" + action);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int songId = preferences.getInt("song", R.raw.song1);
        int nextSongId = songId == R.raw.song1 ? R.raw.song2 : R.raw.song1;

        if (mediaPlayer == null) {
            Log.i("PlayerService", "Setting up media player");
            mediaPlayer = MediaPlayer.create(this, songId);
        }

        if (action.equals(ACTION_START) && !mediaPlayer.isPlaying()) mediaPlayer.start();
        if (action.equals(ACTION_PAUSE) && mediaPlayer.isPlaying()) mediaPlayer.pause();
        if (action.equals(ACTION_STOP)) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(this, songId);
        }
        if (ACTION_SKIP.equals(action)) {
            boolean wasPlaying = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                wasPlaying = true;
            }

            mediaPlayer = MediaPlayer.create(this, nextSongId);
            if (wasPlaying) mediaPlayer.start();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("song", nextSongId);
            editor.apply();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("PlayerService", "Service bound");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i("PlayerService", "Service destroyed");
    }
}

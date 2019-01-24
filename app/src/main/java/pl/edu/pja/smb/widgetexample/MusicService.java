package pl.edu.pja.smb.widgetexample;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

public class MusicService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_START_PAUSE = "pl.edu.pja.smb.widgetexample.action.START_PAUSE";
    public static final String ACTION_STOP = "pl.edu.pja.smb.widgetexample.action.STOP";
    public static final String ACTION_SKIP = "pl.edu.pja.smb.widgetexample.action.SKIP";

    public MusicService() {
        super("MusicService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;

        final String action = intent.getAction();
        if (action == null) return;

        Log.i("MusicService", "Handling action: " + action);

        if (ACTION_START_PAUSE.equals(action)) {

        }
    }

}

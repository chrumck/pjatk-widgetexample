package pl.edu.pja.smb.widgetexample;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FirstWidget extends AppWidgetProvider {
    public static final String ACTION_GO_TO_URL = "pl.edu.pja.smb.widgetexample.action.GO_TO_URL";
    public static final String ACTION_SET_BACKGROUND = "pl.edu.pja.smb.widgetexample.action.SET_BACKGROUND";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Log.i("FirstWidget", "updating widget #: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.i("FirstWidget", "Received " + intent.getAction());

        if (intent.getAction().equals(ACTION_GO_TO_URL)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pja.edu.pl"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }

        if (intent.getAction().equals(ACTION_SET_BACKGROUND)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int resId = preferences.getInt("background", R.drawable.photo2);
            int nextResId = resId == R.drawable.photo1 ? R.drawable.photo2 : R.drawable.photo1;

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), nextResId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget);
            views.setImageViewBitmap(R.id.imgView, bm);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) appWidgetManager.updateAppWidget(appWidgetID, views);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("background", nextResId);
            editor.apply();
        }
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget);

        Intent goToUrlIntent = new Intent(ACTION_GO_TO_URL);
        PendingIntent pendingGoToUrlIntent = PendingIntent.getBroadcast(context, 0, goToUrlIntent, 0);
        views.setOnClickPendingIntent(R.id.goToPjwstkBtn, pendingGoToUrlIntent);

        Intent setBackgroundIntent = new Intent(ACTION_SET_BACKGROUND);
        PendingIntent pendingSetBackgroundIntent = PendingIntent.getBroadcast(context, 0, setBackgroundIntent, 0);
        views.setOnClickPendingIntent(R.id.setBackgroundBtn, pendingSetBackgroundIntent);

        Intent startIntent = new Intent(context, PlayerService.class);
        startIntent.setAction(PlayerService.ACTION_START);
        PendingIntent pendingStartIntent = PendingIntent.getService(context, 0, startIntent, 0);
        views.setOnClickPendingIntent(R.id.startBtn, pendingStartIntent);

        Intent pauseIntent = new Intent(context, PlayerService.class);
        pauseIntent.setAction(PlayerService.ACTION_PAUSE);
        PendingIntent pendingPauseIntent = PendingIntent.getService(context, 0, pauseIntent, 0);
        views.setOnClickPendingIntent(R.id.pauseBtn, pendingPauseIntent);

        Intent stopIntent = new Intent(context, PlayerService.class);
        stopIntent.setAction(PlayerService.ACTION_STOP);
        PendingIntent pendingStopIntent = PendingIntent.getService(context, 0, stopIntent, 0);
        views.setOnClickPendingIntent(R.id.stopBtn, pendingStopIntent);

        Intent skipIntent = new Intent(context, PlayerService.class);
        skipIntent.setAction(PlayerService.ACTION_SKIP);
        PendingIntent pendingSkipIntent = PendingIntent.getService(context, 0, skipIntent, 0);
        views.setOnClickPendingIntent(R.id.skipBtn, pendingSkipIntent);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int resId = preferences.getInt("background", R.drawable.photo1);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        views.setImageViewBitmap(R.id.imgView, bm);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


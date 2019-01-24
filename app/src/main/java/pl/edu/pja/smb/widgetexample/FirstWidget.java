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
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class FirstWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
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

        if (intent.getAction().equals("goToPjwstk")) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pja.edu.pl"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);
        }

        if (intent.getAction().equals("setBackground")) {
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

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.first_widget);

        Intent goToUrlIntent = new Intent("goToPjwstk");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, goToUrlIntent, 0);
        views.setOnClickPendingIntent(R.id.goToPjwstkBtn, pi);

        Intent setBackgroundIntent = new Intent("setBackground");
        PendingIntent pi2 = PendingIntent.getBroadcast(context, 0, setBackgroundIntent, 0);
        views.setOnClickPendingIntent(R.id.setBackgroundBtn, pi2);

        Intent startPauseIntent = new Intent(context, MusicService.class);
        startPauseIntent.setAction(MusicService.ACTION_START_PAUSE);
        PendingIntent pi3 = PendingIntent.getService(context, 0, startPauseIntent, 0);
        views.setOnClickPendingIntent(R.id.startPauseBtn, pi3);

        Intent stopIntent = new Intent(context, MusicService.class);
        stopIntent.setAction(MusicService.ACTION_STOP);
        PendingIntent pi4 = PendingIntent.getService(context, 0, stopIntent, 0);
        views.setOnClickPendingIntent(R.id.stopBtn, pi4);

        Intent skipIntent = new Intent(context, MusicService.class);
        skipIntent.setAction(MusicService.ACTION_SKIP);
        PendingIntent pi5 = PendingIntent.getService(context, 0, skipIntent, 0);
        views.setOnClickPendingIntent(R.id.skipBtn, pi5);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int resId = preferences.getInt("background", R.drawable.photo1);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        views.setImageViewBitmap(R.id.imgView, bm);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


package com.example.vanil_singh.widgets;

/**
 * Created by Vanil-Singh on 9/17/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class WidgetActivity extends AppWidgetProvider {
    private static final String LOG_TAG = "ExampleWidget" ;
    private static final DateFormat df = new SimpleDateFormat( "hh:mm:ss" );

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int [] appWidgetIds) {
        final int N = appWidgetIds. length ;
        Log.d( LOG_TAG , "Updating Example Widgets." );

        for ( int i = 0 ; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, MainActivity. class );
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0 , intent, 0 );

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout. widget_layout );
            views.setOnClickPendingIntent(R.id. button , pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    public static void updateAppWidget(Context context, AppWidgetManager
            appWidgetManager, int appWidgetId) {
        String currentTime = df .format( new Date());
        RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                R.layout. widget_layout );
        updateViews.setTextViewText(R.id. widget1label , currentTime);
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }
    private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent( CLOCK_WIDGET_UPDATE );
        PendingIntent pendingIntent = PendingIntent. getBroadcast (context, 0 ,
                intent, PendingIntent. FLAG_UPDATE_CURRENT );
        return pendingIntent;
    }
    @Override
    public void onDisabled(Context context) {
        super .onDisabled(context);
        Log. d ( LOG_TAG , "Widget Provider disabled. Turning off timer" );
        AlarmManager alarmManager =
                (AlarmManager)context.getSystemService(Context. ALARM_SERVICE );
        alarmManager.cancel(createClockTickIntent(context));
    }
    @Override
    public void onEnabled(Context context) {
        super .onEnabled(context);
        Log. d ( LOG_TAG , "Widget Provider enabled. Starting timer to update widget every second" );
                AlarmManager alarmManager =
                        (AlarmManager)context.getSystemService(Context. ALARM_SERVICE );
        Calendar calendar = Calendar. getInstance ();
        calendar.setTimeInMillis(System. currentTimeMillis ());
        calendar.add(Calendar. SECOND , 10 );
        alarmManager.setRepeating(AlarmManager. RTC , calendar.getTimeInMillis(),
                1000 , createClockTickIntent(context));
    }
    public static String CLOCK_WIDGET_UPDATE =
            "com.hyperiondev.widget.TIME_WIDGET_UPDATE" ;
    @Override
    public void onReceive(Context context, Intent intent) {
        super .onReceive(context, intent);
        if ( CLOCK_WIDGET_UPDATE .equals(intent.getAction())) {

            ComponentName thisAppWidget = new
                    ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for ( int appWidgetID: ids) {
                updateAppWidget(context, appWidgetManager, appWidgetID);
            }
        }
    }

}

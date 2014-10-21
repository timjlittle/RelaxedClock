package uk.me.timlittle.holidayclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HolidayClockAppWidget extends AppWidgetProvider {
	private static final String TAG = "HolidayClockAppWidget";
	private static final int REQUEST_CODE = 1;
    private Context context = null;
    private static final Intent update = new Intent( HolidayClockService.ACTION_UPDATE );

    
    @Override
    public void onUpdate( Context context, 
        AppWidgetManager appWidgetManager, 
        int[] appWidgetIds )
    {
    	Log.d( TAG, "onUpdate" );
        this.context = context;
        this.context.startService( update );
        scheduleTimer();
    }
    
    private void scheduleTimer()
    {
        Calendar date = Calendar.getInstance();
        date.set( Calendar.SECOND, 0 );
        date.set( Calendar.MILLISECOND, 0 );
        date.add( Calendar.MINUTE, 1 );
        AlarmManager am =
                (AlarmManager) context.getSystemService(
                        Context.ALARM_SERVICE );
        PendingIntent pi = PendingIntent.getService( context,
                REQUEST_CODE,
                update,
                PendingIntent.FLAG_NO_CREATE );
        if ( pi == null )
        {
            pi = PendingIntent.getService( context,
                    REQUEST_CODE,
                    update,
                    PendingIntent.FLAG_CANCEL_CURRENT );
            am.setRepeating( AlarmManager.RTC,
                    date.getTimeInMillis(),
                    60 * 1000,
                    pi );
            Log.d( TAG, "Alarm created" );
        }
    }
    

    @Override
    public void onDeleted( Context context,
                           int[] appWidgetIds )
    {
        Log.d( TAG, "onDeleted" );
        AppWidgetManager mgr = AppWidgetManager.getInstance( context );
        int[] remainingIds = mgr.getAppWidgetIds(
                new ComponentName( context, this.getClass() ) );
        if ( remainingIds == null || remainingIds.length <= 0 )
        {
            PendingIntent pi = PendingIntent.getService( context,
                    REQUEST_CODE,
                    update,
                    PendingIntent.FLAG_NO_CREATE );
            if ( pi != null )
            {
                AlarmManager am =
                        (AlarmManager) context.getSystemService(
                                Context.ALARM_SERVICE );
                am.cancel( pi );
                pi.cancel();
                Log.d( TAG, "Alarm cancelled" );
            }
        }
    }    
}

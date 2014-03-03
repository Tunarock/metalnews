package it.polimi.metalnews;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class StartTimer extends Service
{
    Alarm alarm = new Alarm();
    int hour;
    
    public void onCreate()
    {
        super.onCreate();  
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
    	 SharedPreferences sharedPrefs = PreferenceManager
                 .getDefaultSharedPreferences(this);

     		hour= Integer.valueOf(sharedPrefs.getString("prefNotificationFrequency", "NULL"));
     		
    		alarm.SetAlarm(StartTimer.this, hour);
         
     return START_STICKY;
    }



    public void onStart(Context context,Intent intent, int startId)
    {
    	SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 		hour= Integer.valueOf(sharedPrefs.getString("prefNotificationFrequency", "NULL"));
        alarm.SetAlarm(context, hour);
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        return null;
    }
}
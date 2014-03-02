package it.polimi.metalnews;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class StartTimer extends Service
{
	String lastTitle;
    Alarm alarm = new Alarm();
    public void onCreate()
    {
        super.onCreate();  
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
{
    	lastTitle=intent.getStringExtra("lastTitle");
        alarm.SetAlarm(StartTimer.this, lastTitle);
         
     return START_STICKY;
}



    public void onStart(Context context,Intent intent, int startId)
    {
        alarm.SetAlarm(context, lastTitle);
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        return null;
    }
}
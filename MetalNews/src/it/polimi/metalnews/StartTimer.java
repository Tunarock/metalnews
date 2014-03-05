package it.polimi.metalnews;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;

public class StartTimer extends Service
{
	private static final String DEFAULT_CHECK_NEWS_FREQUENCY = "12";
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

		//Controllo se le notifiche sono attive
		if(sharedPrefs.getBoolean("prefNotification", false)){
			
			hour= Integer.valueOf(sharedPrefs.getString("prefNotificationFrequency", DEFAULT_CHECK_NEWS_FREQUENCY));
			alarm.SetAlarm(StartTimer.this, hour);
		}

		return START_STICKY;
	}



	public void onStart(Context context,Intent intent, int startId)
	{
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		//Controllo se le notifiche sono attive
				if(sharedPrefs.getBoolean("prefNotification", false)){
					
					hour= Integer.valueOf(sharedPrefs.getString("prefNotificationFrequency", DEFAULT_CHECK_NEWS_FREQUENCY));
					alarm.SetAlarm(StartTimer.this, hour);
				}
	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
}
package it.polimi.metalnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.widget.Toast;

public class SettingActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		int  hour= Integer.valueOf(sharedPrefs.getString("prefNotificationFrequency", "NULL"));


		Intent i=new Intent(this,StartTimer.class);
		this.stopService(i);

		if(hour>0)
		{

			this.startService(i);
		}

		Toast.makeText(this, "Impostazioni salvate", Toast.LENGTH_SHORT).show();

	}



}

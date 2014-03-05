package it.polimi.metalnews;

import it.polimi.metalnews.activity.MainActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class CheckNewNews extends IntentService {

	private static final int ID_NOTIFICATION = 1;
	private static final String URL_NEWS = "http://metalitalia.com/category/notizie/";
	private final String FILE="listaGruppi.txt";
	private String lastTitle;


	public CheckNewNews() {
		super("CheckNewNews");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
		Log.i("msg", "Service-onStartCommand");


		return super.onStartCommand(intent,flags,startId);
	}


	/**
	 * The IntentService calls this method from the default worker thread with
	 * the intent that started the service. When this method returns, IntentService
	 * stops the service, as appropriate.
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			InputStream inputStream = openFileInput("settings.txt");

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				lastTitle = stringBuilder.toString();
			}
		}
		catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}

		Log.i("msg", "Ultimo titolo: "+lastTitle);
		Log.i("msg", "sto controllando se ci sono nuove notizie");


		try {
			HttpClient client = new DefaultHttpClient();  
			HttpGet get = new HttpGet(URL_NEWS);
			HttpResponse responseGet = client.execute(get);  
			HttpEntity resEntityGet = responseGet.getEntity();  
			if (resEntityGet != null) {  

				String response = EntityUtils.toString(resEntityGet);

				ArrayList<String> title=new ArrayList<String>();
				ArrayList<Info> info=new ArrayList<Info>();
				Document doc=Jsoup.parse(response);

				Element link = doc.getElementById("recent-posts");

				Elements blocks= link.getElementsByClass("box-light");


				int i=0;
				for(Element block: blocks)
				{
					title.add(block.getElementsByClass("text-small").get(0).text());
					info.add(new Info(title.get(i),null,null));
					i++;
				}




				//recupero i nomi dei gruppi da notificare
				ArrayList<String> groups=readFile(FILE);

				//se non ci sono gruppi notifico qualsiasi nuova notizia
				if(groups.size()==0)
				{
					if(info.get(0).getTitle().compareTo(lastTitle)!=0)
						createNotificationNews();
				}
				else
					for(i=0;i<title.size()&& info.get(i).getTitle().compareTo(lastTitle)!=0 ;i++) 
						if(groups.contains(info.get(i).getTitle())){

							createNotificationNews();
							break;

						}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createNotificationNews() {
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.launcher)
		.setContentTitle("MetalNews")
		.setContentText("Ci sono nuove notizie");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
						);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
	}


	public ArrayList<String> readFile(String file)
	{
		ArrayList<String> groups=new ArrayList<String>();

		try {
			InputStream inputStream = openFileInput(file);

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					groups.add(receiveString.toUpperCase()+":");
				}

				inputStream.close();
			}
		}
		catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		return groups;

	}




}

package it.polimi.metalnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity implements AnimationListener {

	private static final int NEWS_LENGTH = 35;
	private static final String URL_NEWS = "http://metalitalia.com/category/notizie/";

	private static final int SET_ON_CONNECTION = 1;
	private static final int ANIMATION_TIME = 5000;


	private Info[] news;

	private TextView noConnectionText;
	private Button noConnectionButton;
	protected boolean isSuccededNews;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isSuccededNews= false;

		noConnectionText = (TextView) findViewById(R.id.no_connectiontext);
		noConnectionText.setVisibility(View.GONE);
		noConnectionButton = (Button) findViewById(R.id.no_connection);
		noConnectionButton.setVisibility(View.GONE);
		noConnectionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), SET_ON_CONNECTION);

			}
		});

		initializeViews();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("LIFE", "resume");
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("LIFE", "pause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFE", "stop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFE", "destroy");
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i("LIFE", "resultImmediate");

		if(requestCode == SET_ON_CONNECTION){
			if(resultCode == RESULT_OK){
				Log.i("LIFE", "result");
				checkConnection();
			}
		}

	}

	private void initializeViews() throws NotFoundException {

//		checkConnection();
	}

	private void checkConnection() {
		ImageView ivLogo = (ImageView) findViewById(R.id.main_logo);

		if(isOnline()){
			Animation animLogo = AnimationUtils.loadAnimation(this, R.anim.fadein);
			animLogo.setAnimationListener(this);
			ivLogo.startAnimation(animLogo);

		}else{
			ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.noconnection));
			noConnectionButton.setText("Attiva connessione");
			noConnectionButton.setVisibility(View.VISIBLE);
			noConnectionText.setVisibility(View.VISIBLE);			
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {

		if(isSuccededNews)
			startHome();
		else{
			ImageView ivLogo = (ImageView) findViewById(R.id.main_logo);
			ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.noconnection));
			noConnectionButton.setText("Attiva connessione");
			noConnectionButton.setVisibility(View.VISIBLE);
			noConnectionText.setVisibility(View.VISIBLE);	
		}
	}

	private void startHome() {

		Intent intent = new Intent(this, HomeActivity.class);

		intent.putExtra("news", news);

		startActivity(intent);
	}

	private Info[] createInfoBundle(int n, String[] titles, String[] targetUrl, String[] imageUrls){

		Info[] info = new Info[n];

		for(int cont=0;cont<n;cont++)
		{			
			info[cont]=new Info(titles[cont],imageUrls[cont], targetUrl[cont]);	
		}

		return info;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {

		Log.i("SUCCESSTHREAD", "START-ANIMATION");

		AsyncHttpClient clientNews = new AsyncHttpClient();
		clientNews.setTimeout(ANIMATION_TIME);
		clientNews.get(URL_NEWS,getNewsResponseHandler());

	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private AsyncHttpResponseHandler getNewsResponseHandler(){

		return new AsyncHttpResponseHandler() {			

			@Override
			public void onStart(){

				Log.i("SUCCESSTHREAD", "START-NEWS");

			}

			@Override
			public void onSuccess(String response) {

				Log.i("SUCCESSTHREAD", "news");



				String[] imageUrls= new String[NEWS_LENGTH];	
				String[] titles = new String[NEWS_LENGTH];
				String[] targetUrl = new String[NEWS_LENGTH];

				Document doc=Jsoup.parse(response);

				Element link = doc.getElementById("recent-posts");

				Elements blocks= link.getElementsByClass("box-light");

				int i=0;
				for(Element block: blocks)
				{
					titles[i]=block.getElementsByClass("text-small").get(0).text();
					targetUrl[i]=block.getElementsByClass("text-small").get(0).attr("href");
					imageUrls[i]=block.getElementsByTag("img").get(0).attr("src");					

					i++;
				}

				news = createInfoBundle(NEWS_LENGTH, titles,targetUrl,imageUrls);

				isSuccededNews = true;


			}

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

				//DISPLAY A NOTIFICATION OR A BUTTON TO SAY THAT MAYBE WIRELESS IS NOT ACTIVATED
				Log.i("FAILTHREAD", "news");

			}
		};
	}
}

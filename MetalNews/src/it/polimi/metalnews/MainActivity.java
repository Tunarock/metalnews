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
import android.util.Log;
import android.view.View;
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
	private static final String URL_ALBUM = "http://metalitalia.com/category/album/";
	private static final String URL_CONTEST = "http://metalitalia.com/category/contest/";
	private static final int ANIMATION_TIME = 5000;
	protected static final int ALBUM_CONTEST_LENGTH = 20;

	private Info[] news;
	private Info[] album;
	private Info[] contests;

	private TextView noConnectionText;
	private Button noConnectionButton;
	protected boolean isSuccededAlbum ;
	protected boolean isSuccededContest;
	protected boolean isSuccededNews;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isSuccededAlbum = false;
		isSuccededContest= false;
		isSuccededNews= false;

		noConnectionText = (TextView) findViewById(R.id.no_connectiontext);
		noConnectionText.setVisibility(View.GONE);
		noConnectionButton = (Button) findViewById(R.id.no_connection);
		noConnectionButton.setVisibility(View.GONE);
		initializeViews();

	}

	private void initializeViews() throws NotFoundException {

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

		
	}

	private void startHome() {
		
		Intent intent = new Intent(this, HomeActivity.class);

		intent.putExtra("news", news);
		intent.putExtra("album", album);
		intent.putExtra("contest", contests);

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

		AsyncHttpClient clientContest = new AsyncHttpClient();
		clientContest.setTimeout(ANIMATION_TIME);
		clientContest.get(URL_CONTEST,getContestResponseHandler());

		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.setTimeout(ANIMATION_TIME);
		clientAlbum.get(URL_ALBUM,getAlbumResponseHandler());

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
				if(isSuccededAlbum && isSuccededContest)
					startHome();

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

	private AsyncHttpResponseHandler getAlbumResponseHandler(){

		return new AsyncHttpResponseHandler() {			

			public void onStart(){

				Log.i("SUCCESSTHREAD", "START-ALBUM");

			}

			@Override
			public void onSuccess(String response) {
				Log.i("SUCCESSTHREAD", "album");
				
				album = getArrayInfoFromHtml(response);

				isSuccededAlbum = true;
				if(isSuccededContest && isSuccededNews)
					startHome();

			}

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

				//DISPLAY A NOTIFICATION OR A BUTTON TO SAY THAT MAYBE WIRELESS IS NOT ACTIVATED
				Log.i("FAILTHREAD", "album");

			}
		};
	}

	private AsyncHttpResponseHandler getContestResponseHandler(){

		return new AsyncHttpResponseHandler() {			

			public void onStart(){

				Log.i("SUCCESSTHREAD", "START-CONTEST");

			}

			@Override
			public void onSuccess(String response) {
				Log.i("SUCCESSTHREAD", "contest");			

				contests = getArrayInfoFromHtml(response);
				isSuccededContest = true;
				
				if(isSuccededAlbum && isSuccededNews)
					startHome();

			}

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

				//DISPLAY A NOTIFICATION OR A BUTTON TO SAY THAT MAYBE WIRELESS IS NOT ACTIVATED
				Log.i("FAILTHREAD", "contest");

			}
		};
	}

	// restituisce solo array o di album o contest!!!!!
	private Info[] getArrayInfoFromHtml(String response) {


		String[] imageUrls= new String[ALBUM_CONTEST_LENGTH];	
		String[] titles = new String[ALBUM_CONTEST_LENGTH];
		String[] targetUrl = new String[ALBUM_CONTEST_LENGTH];

		Document doc=Jsoup.parse(response);
		Element link = doc.getElementById("recent-posts");

		Elements blocks= link.getElementsByClass("entry");

		int i=0;
		for(Element block: blocks)
		{
			titles[i]=block.getElementsByClass("title").get(0).text();
			targetUrl[i]=block.getElementsByClass("title").get(0).getElementsByTag("a").get(0).attr("href");
			Elements elementsByTag = block.getElementsByTag("img");
			if(elementsByTag.size()>0)
				imageUrls[i]=elementsByTag.get(0).attr("src");					

			i++;
		}

		Log.i("getdata", "getdata");

		return createInfoBundle(ALBUM_CONTEST_LENGTH, titles,targetUrl,imageUrls);
	}

}

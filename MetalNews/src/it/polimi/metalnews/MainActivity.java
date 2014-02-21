package it.polimi.metalnews;

import java.util.ArrayList;

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
	private static final String URL = "http://metalitalia.com/category/notizie/";
	private static final int ANIMATION_TIME = 5000;

	String[] imageUrls= new String[NEWS_LENGTH];	
	String[] titles = new String[NEWS_LENGTH];
	String[] targetUrl = new String[NEWS_LENGTH];

	private AsyncHttpResponseHandler responseHandler;
	private boolean isSucceded = false;
	private AsyncHttpClient client;
	
	private TextView noConnectionText;
	private Button noConnectionButton;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

		//controllo che il task sia andato a buon fine
		if(isSucceded){
			
			Info[] news = createInfoBundle(NEWS_LENGTH); 
			
			Intent intent = new Intent(this, HomeActivity.class);
			
			intent.putExtra("news", news);
//			intent.putExtra("titles", titles);
//			intent.putExtra("targetUrl", targetUrl);
			startActivity(intent);
		}else{

			//gestisco la failure
		}
	}
	
	private Info[] createInfoBundle(int n){
		
		Info[] news = new Info[n];
		
		for(int cont=0;cont<35;cont++)
		{			
			news[cont]=new Info(titles[cont],imageUrls[cont], targetUrl[cont]);	
		}
		
		return news;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		
			client = new AsyncHttpClient();
			client.setTimeout(ANIMATION_TIME);
			responseHandler = getNewResponseHandler() ;
			client.get(URL,responseHandler);			

	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private AsyncHttpResponseHandler getNewResponseHandler(){

		return new AsyncHttpResponseHandler() {			

			@Override
			public void onSuccess(String response) {

				isSucceded = true;

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

			}

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

				//DISPLAY A NOTIFICATION OR A BUTTON TO SAY THAT MAYBE WIRELESS IS NOT ACTIVATED


			}
		};
	}

}

package it.polimi.metalnews;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

	String[] imageUrls= new String[35];
	
	String[] titles = new String[35];
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initializeViews();
		
		
		
		
		AsyncHttpClient client = new AsyncHttpClient();
		String url="http://metalitalia.com/category/notizie/";

			client.get(url,new AsyncHttpResponseHandler() {
				
				public void onStart(){
				
				}
				
				@Override
				public void onSuccess(String response) {
					
					
					Document doc=Jsoup.parse(response);
				
					Element link = doc.getElementById("recent-posts");
					
					Elements blocks= link.getElementsByClass("box-light");
					
					int i=0;
			
					
					for(Element block: blocks)
					{
						imageUrls[i] = block.getElementsByTag("img").get(0).attr("src");
						titles[i] = block.getElementsByClass("text-small").get(0).text();
						i++;
						
						Log.e(Integer.toString(i), "msg");
						Log.e(imageUrls[i], "msg");
						Log.e(titles[i], "msg");
					}
					
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					intent.putExtra("imageUrls", imageUrls);
					intent.putExtra("titles", titles);
					startActivity(intent);
					

				}
				
				@Override
				public void onFailure(int statusCode,
						org.apache.http.Header[] headers,
						byte[] responseBody,
						java.lang.Throwable error) {

					
				}
			});
		
		
		
		
		
				
	}

	private void initializeViews() throws NotFoundException {
		
		ImageView ivLogo = (ImageView) findViewById(R.id.main_logo);
		Animation animLogo = AnimationUtils.loadAnimation(this, R.anim.fadein);		
		ivLogo.startAnimation(animLogo);
	}
	
}

package it.polimi.metalnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class ContestActivity extends Activity implements OnInitializedListener, ConnectionCallbacks, OnConnectionFailedListener {

	private String id;

	private static final String RECENT_POSTS = "recent-posts";

	private static final String ENTRY_CONTENT = "entry-content";

	Info info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contest);


		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN)
		.build();

		Intent intent = getIntent();
		String contestHtml = intent.getStringExtra("contestHtml");

		Bundle bundle = intent.getExtras();
		info=(Info)bundle.getParcelable("info");

		TextView title= (TextView) findViewById(R.id.contest_title);
		title.setText(info.getTitle()+ " " + info.getBacktitle());

		dispatch(contestHtml);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.gplus:
			shareOnGPlus();
			return true;

		}
		return false;
	}

	private void dispatch(String content){

		Document doc = Jsoup.parse(content);

		Elements entryContent = doc.getElementsByClass(ENTRY_CONTENT);

		Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 


		LinearLayout rl = (LinearLayout) findViewById(R.id.contest_layout);
		LayoutInflater li=getLayoutInflater();

		for(Element p: paragraphs)
		{
			analyzer(p, rl, li);
		}

	}


	private void analyzer (Element e, LinearLayout ll, LayoutInflater li){

		if(e.hasText()){
			View view = li.inflate(R.layout.single_text_element, ll, false);
			TextView tw= (TextView) view.findViewById(R.id.text);
			tw.setText(Html.fromHtml(e.html(), null ,null));
			ll.addView(tw);			
		}else{
			if(!e.getElementsByTag("img").isEmpty()){
				View view = li.inflate(R.layout.single_image_element, ll, false);
				ImageView iv= (ImageView) view.findViewById(R.id.img);
				ll.addView(iv);
				ImageDownloadHtml imgDown= new ImageDownloadHtml(iv);
				imgDown.execute(e.getElementsByTag("img").get(0).attr("src"));
			}
			else if(!e.getElementsByTag("iframe").isEmpty()){
				String url=e.getElementsByTag("iframe").get(0).attr("src");
				id=url.substring(29, 40);

				View view = li.inflate(R.layout.single_youtube, ll, false);

				YouTubePlayerView youTubeView = (YouTubePlayerView) view.findViewById(R.id.youtube_view);	  
				ll.addView(view);
				youTubeView.initialize(DeveloperKey.DEVELOPER_KEY,this);

			}


		}


	}

	private void shareOnGPlus() {

		// Launch the Google+ share dialog with attribution to your app.
		Intent shareIntent = new PlusShare.Builder(this)
		.setType("text/plain")
		.setText("Welcome to the Google+ platform.")
		.setContentUrl(Uri.parse(info.getTargetUrl()))
		.getIntent();

		startActivityForResult(shareIntent, 0);

	}
	
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub

	}

}

package it.polimi.metalnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
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

public class NewsActivity extends YouTubeFailureRecoveryActivity implements OnInitializedListener, ConnectionCallbacks, OnConnectionFailedListener{

	private static final String ENTRY_CONTENT = "entry-content";

	private String id;
	private UiLifecycleHelper uiHelper;
	private Info info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		uiHelper = new UiLifecycleHelper(this, new StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub
				
			}
		});
		
		uiHelper.onCreate(savedInstanceState);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN)
		.build();

		Intent intent = getIntent();
		String newsHtml = intent.getStringExtra("newsHtml");

		Bundle bundle = intent.getExtras();
		info=(Info)bundle.getParcelable("info");

		dispatch(newsHtml);


	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
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
		case R.id.fb:
			shareFacebook();
			return true;

		}
		return false;
	}

	private void shareFacebook() {
		if (FacebookDialog.canPresentShareDialog(getApplicationContext(), 
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
			.setLink(info.getTargetUrl())
			.build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {
			// Fallback. For example, publish the post using the Feed Dialog
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});
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

	private void dispatch(String content){

		Document doc = Jsoup.parse(content);


		Elements entryContent = doc.getElementsByClass(ENTRY_CONTENT);

		Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 

		LinearLayout rl = (LinearLayout) findViewById(R.id.news_layout);
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

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {

			player.cueVideo(id);
		}
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		return (YouTubePlayerView) findViewById(R.id.youtube_view);

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

}

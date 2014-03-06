package it.polimi.metalnews.activity;

import it.polimi.metalnews.DeveloperKey;
import it.polimi.metalnews.ImageDownloadHtml;
import it.polimi.metalnews.Info;
import it.polimi.metalnews.R;

import org.jsoup.nodes.Element;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class InfoActivity extends YouTubeFailureRecoveryActivity implements OnInitializedListener,ConnectionCallbacks, OnConnectionFailedListener {

	private static final int YOUTUBE_ID_LENGTH = 11;

	private ProgressDialog bar;

	protected Info info;
	private String id;
	protected static final String ENTRY_CONTENT = "entry-content";

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	/*Gestore Facebook*/
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		//		bar = ProgressDialog.show(this,"", "content loading");
		//
		//		bar.getWindow().setGravity(Gravity.BOTTOM);

		setContentView(R.layout.background_black_logo);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		Bundle bundle = getIntent().getExtras();
		info=(Info)bundle.getParcelable("info");

		getInfoFromHtml();

		setUiHelper(savedInstanceState);

		setGoogleClient();


	}

	protected abstract void parseResponse(String content);


	protected void parseFooter(String publish, LinearLayout ll, LayoutInflater li) {

		View view = li.inflate(R.layout.single_text_element, ll, false);
		TextView tw= (TextView) view.findViewById(R.id.text);


		StringBuilder sb = new StringBuilder(publish);
		int index = sb.indexOf("|");
		tw.setText(Html.fromHtml(sb.substring(0,index), null ,null));
		ll.addView(tw);			
	}

	protected void analyzer (Element e, LinearLayout ll, LayoutInflater li){

		View view;

		if(e.hasText()){
			if(e.attr("class").compareTo("meta-info")!=0){
				view = li.inflate(R.layout.single_text_element, ll, false);
				TextView tw= (TextView) view.findViewById(R.id.text);
				tw.setText(Html.fromHtml(e.html(), null ,null));
				tw.setLinkTextColor(getResources().getColor(R.color.GreenMetal));
				tw.setMovementMethod(LinkMovementMethod.getInstance());
				ll.addView(tw);
			}else{
				parseFooter(e.text(), ll, li);
			}
		}else{
			if(!e.getElementsByTag("img").isEmpty()){
				view = li.inflate(R.layout.single_image_element, ll, false);
				ImageView iv= (ImageView) view.findViewById(R.id.img);
				ll.addView(iv);
				ImageDownloadHtml imgDown= new ImageDownloadHtml(iv);
				imgDown.execute(e.getElementsByTag("img").get(0).attr("src"));
			}
			else if(!e.getElementsByTag("iframe").isEmpty()){
				String url=e.getElementsByTag("iframe").get(0).attr("src");
				if (isYouTubeUrl(url)) {
					int start=url.indexOf("embed/")+6;
					id=url.substring(start, start+YOUTUBE_ID_LENGTH);
					view = li.inflate(R.layout.single_youtube, ll, false);
					YouTubePlayerView youTubeView = (YouTubePlayerView) view
							.findViewById(R.id.youtube_view);
					ll.addView(view);
					youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
				}else if (isSoundCloudUrl(url)){

					WebView wb = (WebView) li.inflate(R.layout.soundcloud, ll, false);
					String html = e.getElementsByTag("iframe").get(0).attr("src");
					wb.loadUrl(html);
					ll.addView(wb);


				}

			}


		}


	}

	private boolean isYouTubeUrl(String url) {
		// TODO Auto-generated method stub
		return url.contains("youtube");
	}

	private boolean isSoundCloudUrl(String url) {
		// TODO Auto-generated method stub
		return url.contains("soundcloud");
	}

	protected abstract void setLayout();

	protected void getInfoFromHtml(){


		AsyncHttpClient client = new AsyncHttpClient();

		client.get(info.getTargetUrl(),new AsyncHttpResponseHandler(){

			@Override
			public void onSuccess(String response) {

				//				bar.dismiss();
				getActionBar().show();
				setLayout();
				parseResponse(response);

			}
			//INSERIRE UNA PROGRESS BAR!!!
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
			}

			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

			}


		});

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

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	//FB
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

	private void setUiHelper(Bundle savedInstanceState) {
		uiHelper = new UiLifecycleHelper(this, new StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub

			}
		});

		uiHelper.onCreate(savedInstanceState);
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
	//END FB

	//YOUTUBE
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
	//END YOUTUBE

	//GOOGLE +
	private void shareOnGPlus() {

		// Launch the Google+ share dialog with attribution to your app.
		Intent shareIntent = new PlusShare.Builder(this)
		.setType("text/plain")
		.setText("Welcome to the Google+ platform.")
		.setContentUrl(Uri.parse(info.getTargetUrl()))
		.getIntent();

		startActivityForResult(shareIntent, 0);

	}

	private void setGoogleClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.addApi(Plus.API, null)
		.addScope(Plus.SCOPE_PLUS_LOGIN)
		.build();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}
	//END GOOGLE +
}

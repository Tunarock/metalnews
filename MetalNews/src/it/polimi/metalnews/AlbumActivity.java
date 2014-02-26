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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusShare;

public class AlbumActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	Info info;

	TextView title;
	TextView backtitle;
	TextView grade;
	TextView publisher;
	TextView review_content;
	TextView tracklist_content;
	
	ImageView img;

	private static final String ENTRY_CONTENT = "entry-content";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);

		Intent intent = getIntent();

		String albumHtml = intent.getStringExtra("albumHtml");

		Bundle bundle = intent.getExtras();
		info=(Info)bundle.getParcelable("info");		

		title= (TextView) findViewById(R.id.album_title);
		backtitle= (TextView) findViewById(R.id.album_backtitle);
		grade= (TextView) findViewById(R.id.album_grade);
		publisher= (TextView) findViewById(R.id.album_publisher);
		review_content= (TextView) findViewById(R.id.album_review_content);
		tracklist_content = (TextView) findViewById(R.id.album_tracklist_content);
		img = (ImageView) findViewById(R.id.album_img);


		title.setText(info.getTitle());		
		backtitle.setText(info.getBacktitle());



		ImageDownloadHtml imgDown= new ImageDownloadHtml(img);
		imgDown.execute(info.getImgUrl());

		review_content.setMaxLines(8);
		tracklist_content.setMaxLines(8);

		parse(albumHtml);

		review_content.setOnClickListener(new OnClickListener() {

			private boolean isReviewExpanded = false;
			private ImageView collapser = (ImageView) findViewById(R.id.album_review_collapse);

			@Override
			public void onClick(View v) {
				TextView t = (TextView) v;
				if(!isReviewExpanded){
					collapser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_collapse));
					isReviewExpanded=true;
					t.setMaxLines(10000);
					
				}else{
					collapser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_expand));
					isReviewExpanded=false;
					t.setMaxLines(8);
				}

			}
		});
		tracklist_content.setOnClickListener(new OnClickListener() {

			private boolean isTracklistExpanded = false;
			private ImageView collapser = (ImageView) findViewById(R.id.album_tracklist_collapse);

			@Override
			public void onClick(View v) {
				TextView t = (TextView) v;
				if(!isTracklistExpanded){
					collapser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_collapse));
					isTracklistExpanded=true;
					t.setMaxLines(10000);
				}else{
					collapser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_expand));
					isTracklistExpanded=false;
					t.setMaxLines(8);
				}
			}
		});
	}

	private void parse(String content){

		Document doc = Jsoup.parse(content);

		Element entryContent = doc.getElementsByClass(ENTRY_CONTENT).get(0);

		Elements meta_info = entryContent.getElementsByClass("meta-info").get(0).getElementsByTag("a"); 
		publisher.setText("Pubblicato da: "+meta_info.get(0).text());

		Element grade = entryContent.getElementById("vote");
		setGrade(grade.text());

		Element par = entryContent.getElementsByTag("p").get(0);
		review_content.setText(par.text());

		Element tracklist = entryContent.getElementsByClass("learn-more-content").get(0);
		tracklist_content.setText(Html.fromHtml(tracklist.html(), null ,null));

	}

	private void setGrade(String text) {

		StringBuilder str=new StringBuilder(text);
		int endTitle=str.indexOf(":");
		grade.setText(str.substring(endTitle+2, str.length()));

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


}

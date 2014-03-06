package it.polimi.metalnews.activity;

import it.polimi.metalnews.ImageDownloadHtml;
import it.polimi.metalnews.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumActivity extends InfoActivity {

	private final int layout = R.layout.activity_album;
	
	TextView title;
	TextView backtitle;
	TextView grade;
	TextView publisher;
	TextView review_content;
	TextView tracklist_content;
	
	ImageView img;

	private static final String ENTRY_CONTENT = "entry-content";

	private void parse(String response){

		Document doc = Jsoup.parse(response);

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
	protected void setLayout() {
		setContentView(layout);
		getActionBar().setTitle(R.string.album);
	}

	@Override
	protected void parseResponse(String response) {
		
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

		parse(response);

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

}

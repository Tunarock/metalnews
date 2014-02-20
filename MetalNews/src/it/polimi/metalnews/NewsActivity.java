package it.polimi.metalnews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsActivity extends Activity {

	private static final String ENTRY_CONTENT = "entry-content";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		Intent intent = getIntent();

		String newsHtml = intent.getStringExtra("newsHtml");
		//		String newsTitle = intent.getStringExtra("title");	


		//Strutturadati sd = new Dispatcher(newsHtml).getStrutturaDati();
		dispatch(newsHtml);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
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


		}


	}
}

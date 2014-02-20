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
import android.widget.LinearLayout;
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

		LinearLayout ll = (LinearLayout) findViewById(R.id.news_layout);
		LayoutInflater li=getLayoutInflater();
		View view;

		String newContent="<html>";
		for(Element p: paragraphs)
		{
			newContent+="<p>"+p.html()+"</p>";
		
		}
		newContent+="</html>";
	
		view = li.inflate(R.layout.single_text_element, ll, false);
		TextView tw= (TextView) view.findViewById(R.id.text);
		tw.setText(Html.fromHtml(newContent, new UrlImageParser(view, null) ,null));
		ll.addView(tw);




	}
}

package it.polimi.metalnews;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
		
		ArrayList<TextView> tw = new ArrayList<TextView>();
		
		LinearLayout ll = (LinearLayout) findViewById(R.layout.activity_news);
		
		for(Element p: paragraphs){
			
			TextView t = new TextView(this);
			t.setText(p.text());
			
			ll.addView(t);
			
		}
		
	}
}

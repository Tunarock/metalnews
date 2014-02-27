package it.polimi.metalnews.activity;

import it.polimi.metalnews.R;
import it.polimi.metalnews.R.id;
import it.polimi.metalnews.R.layout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class NewsActivity extends InfoActivity{

	private int layout = R.layout.activity_info;	

	@Override
	protected void setLayout() {
		setContentView(layout);
		
	}

	@Override
	protected void parseResponse(String content) {
		

			Document doc = Jsoup.parse(content);

			Elements entryContent = doc.getElementsByClass(InfoActivity.ENTRY_CONTENT);

			Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 

			LinearLayout rl = (LinearLayout) findViewById(R.id.news_layout);
			LayoutInflater li=getLayoutInflater();

			for(Element p: paragraphs)
			{
				analyzer(p, rl, li);
			}



		}	

}

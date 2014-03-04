package it.polimi.metalnews.activity;

import it.polimi.metalnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsActivity extends InfoActivity{

	private int layout = R.layout.activity_info;	

	@Override
	protected void setLayout() {
		setContentView(layout);
		TextView title= (TextView) findViewById(R.id.info_title);
		title.setText(info.getTitle()+ " " + info.getBacktitle());
		
	}

	@Override
	protected void parseResponse(String content) {
		

			Document doc = Jsoup.parse(content);

			Elements entryContent = doc.getElementsByClass(InfoActivity.ENTRY_CONTENT);

			Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 

			LinearLayout ll = (LinearLayout) findViewById(R.id.info_layout);
			
			LayoutInflater li=getLayoutInflater();
			
			for(Element p : paragraphs)
			{
				analyzer(p, ll, li);
			}


		}	

}

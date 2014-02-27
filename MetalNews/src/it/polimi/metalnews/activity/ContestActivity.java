package it.polimi.metalnews.activity;

import it.polimi.metalnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContestActivity extends InfoActivity {

	private final int layout = R.layout.activity_info;

	@Override
	protected void parseResponse(String content) {
		

			Document doc = Jsoup.parse(content);

			Elements entryContent = doc.getElementsByClass(InfoActivity.ENTRY_CONTENT);

			Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 

			LinearLayout rl = (LinearLayout) findViewById(R.id.info_layout);
			LayoutInflater li=getLayoutInflater();

			for(Element p: paragraphs)
			{
				analyzer(p, rl, li);
			}



		}

	@Override
	protected void setLayout() {
		setContentView(layout);
		
		TextView title= (TextView) findViewById(R.id.info_title);
		title.setText(info.getTitle()+ " " + info.getBacktitle());
		
	}

}

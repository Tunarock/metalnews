package it.polimi.metalnews.activity;

import it.polimi.metalnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContestActivity extends InfoActivity {

	private final int layout = R.layout.activity_info;
	protected String CONTEST_EMAIL = "concorsi@metalitalia.com";

	@Override
	protected void parseResponse(String content) {
		

			Document doc = Jsoup.parse(content);

			Elements entryContent = doc.getElementsByClass(InfoActivity.ENTRY_CONTENT);

			Elements paragraphs = entryContent.get(0).getElementsByTag("p"); 

			LinearLayout ll = (LinearLayout) findViewById(R.id.info_layout);
			LayoutInflater li=getLayoutInflater();

			for(Element p: paragraphs)
			{
				analyzer(p, ll, li);
			}

			addContestButton(entryContent,li,ll);
			
			
		}

	private void addContestButton(Elements entryContent, LayoutInflater li, LinearLayout ll) {
		String initial = "“CONTEST";
		
		final Elements contests = entryContent.get(0).getElementsContainingOwnText(initial);
		
		if (contests.size()>0) {
			View view = li.inflate(R.layout.contest_button, ll, false);
			TextView tw = (TextView) view.findViewById(R.id.text);
			tw.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final Intent emailIntent = new Intent(
							android.content.Intent.ACTION_SEND);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { CONTEST_EMAIL });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							extractString(contests));
					emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
							"Messaggio...");

					//L'allegato è opzionale
					//				Uri attachment = Uri.parse("file:///sdcard/file_da_allegare.txt");
					//				emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);

					startActivity(Intent.createChooser(emailIntent,
							"Invia email..."));
					;

				}

				private String extractString(Elements contests) {

					String completeText = contests.get(0).text();
					StringBuilder str = new StringBuilder(completeText);
					int start = str.indexOf("“");
					int end = str.indexOf("”");

					return str.substring(start + 1, end);
				}
			});
			ll.addView(tw);
		}
		
	}

	@Override
	protected void setLayout() {
		setContentView(layout);
		
		TextView title= (TextView) findViewById(R.id.info_title);
		title.setText(info.getTitle()+ " " + info.getBacktitle());
		
	}

}

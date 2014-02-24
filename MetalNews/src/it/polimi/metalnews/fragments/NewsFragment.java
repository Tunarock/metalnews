package it.polimi.metalnews.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.NewsActivity;
import android.content.Intent;
import android.os.Bundle;




public class NewsFragment extends InfoFragment{
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		setInfoAdapter();

	}
	
	public NewsFragment(Info[] info,String url,int size)
	{
		super();
		this.info=info;
		this.url = url;
		this.size= size;

	}

		@Override
		protected void startIntentFromListViewElement(String response, Info info) {
			Intent i=new Intent(getActivity(), NewsActivity.class);
			i.putExtra("info", info);
			i.putExtra("newsHtml", response);
			startActivity(i);
			
		}
		
		
		@Override
		protected Info[] getArrayInfoFromHtml(String response) {
			String[] imageUrls= new String[size];	
			String[] titles = new String[size];
			String[] targetUrl = new String[size];

			Document doc=Jsoup.parse(response);

			Element link = doc.getElementById("recent-posts");

			Elements blocks= link.getElementsByClass("box-light");

			int i=0;
			for(Element block: blocks)
			{
				titles[i]=block.getElementsByClass("text-small").get(0).text();
				targetUrl[i]=block.getElementsByClass("text-small").get(0).attr("href");
				imageUrls[i]=block.getElementsByTag("img").get(0).attr("src");					

				i++;
			}

			return createInfoBundle(size, titles,targetUrl,imageUrls);
			
		}
	}

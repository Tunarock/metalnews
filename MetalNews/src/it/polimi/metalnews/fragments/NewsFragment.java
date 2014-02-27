package it.polimi.metalnews.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.activity.NewsActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends InfoFragment{

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		setInfoAdapter();

	}
	
	public NewsFragment(){
		super();
	}

	public NewsFragment(Info[] info,String url,int size)
	{
		super();
		this.info=info;
		this.url = url;
		this.size= size;

	}

	@Override
	protected void startIntentFromListViewElement(Info info) {
		Intent i=new Intent(getActivity(), NewsActivity.class);
		i.putExtra("info", info);			
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


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("LIFE", "NEWS - onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.i("LIFE", "NEWS - start");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("LIFE", "NEWS - resume");
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("LIFE", "NEWS - pause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFE", "NEWS - stop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFE", "NEWS - destroy");
	}
}

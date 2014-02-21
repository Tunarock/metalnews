package it.polimi.metalnews.fragments;

import com.loopj.android.http.AsyncHttpClient;

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
	
	public NewsFragment(Info[] info)
	{
		super();
		this.info=info;

	}

		@Override
		protected void startIntentFromListViewElement(String response, Info info) {
			Intent i=new Intent(getActivity(), NewsActivity.class);
			i.putExtra("info", info);
			i.putExtra("newsHtml", response);
			startActivity(i);
			
		}
	}

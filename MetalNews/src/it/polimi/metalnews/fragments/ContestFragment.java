package it.polimi.metalnews.fragments;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.NewsActivity;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;


public class ContestFragment extends InfoFragment {


	
	protected static final int ALBUM_CONTEST_LENGTH = 20;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);


		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.get(url,getAlbumContestResponseHandler());

		setListShown(false);
		
	}
	
	
	public ContestFragment(String url) {
		super(url);
	}


	@Override
	protected void startIntentFromListViewElement(String response, Info info) {
		Intent i=new Intent(getActivity(), NewsActivity.class);
		i.putExtra("info", info);
		i.putExtra("newsHtml", response);
		startActivity(i);
		
	}
	

}
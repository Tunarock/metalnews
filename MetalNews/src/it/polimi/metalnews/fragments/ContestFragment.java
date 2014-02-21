package it.polimi.metalnews.fragments;

import com.loopj.android.http.AsyncHttpClient;

import it.polimi.metalnews.AlbumActivity;
import android.content.Intent;
import android.os.Bundle;


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
	protected void startIntentFromListViewElement(String response) {
		Intent i=new Intent(getActivity(), AlbumActivity.class);
		i.putExtra("albumHtml", response);
		startActivity(i);
		
	}
	

}
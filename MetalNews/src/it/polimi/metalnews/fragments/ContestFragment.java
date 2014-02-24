package it.polimi.metalnews.fragments;

import it.polimi.metalnews.ContestActivity;
import it.polimi.metalnews.Info;
import android.content.Intent;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;


public class ContestFragment extends InfoFragment {

	private static final int ALBUM_LENGTH = 20;
	
	
	public ContestFragment(String urlContest, int contestLength) {
		super(urlContest, contestLength);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);


		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.get(url,getAlbumContestResponseHandler());

		setListShown(false);
		
	}

	@Override
	protected void startIntentFromListViewElement(String response, Info info) {
		Intent i=new Intent(getActivity(), ContestActivity.class);
		i.putExtra("info", info);
		i.putExtra("contestHtml", response);
		startActivity(i);
		
	}
	

}
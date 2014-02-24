package it.polimi.metalnews.fragments;

import com.loopj.android.http.AsyncHttpClient;

import it.polimi.metalnews.AlbumActivity;
import it.polimi.metalnews.Info;
import android.content.Intent;
import android.os.Bundle;




public class AlbumFragment extends InfoFragment {


	protected static final int ALBUM_LENGTH = 20;

	public AlbumFragment(String urlAlbum, int albumLength) {
		super(urlAlbum,albumLength);
	}


	@Override
	protected void startIntentFromListViewElement(String response, Info info) {
		Intent i=new Intent(getActivity(), AlbumActivity.class);
		i.putExtra("info", info);
		i.putExtra("albumHtml", response);
		startActivity(i);
		
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);


		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.get(url,getAlbumContestResponseHandler());

		setListShown(false);
		
	}
	

}

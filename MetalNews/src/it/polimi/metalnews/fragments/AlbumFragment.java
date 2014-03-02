package it.polimi.metalnews.fragments;

import com.loopj.android.http.AsyncHttpClient;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.activity.AlbumActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;




public class AlbumFragment extends InfoFragment {

	public AlbumFragment(String urlAlbum, int albumLength) {
		super(urlAlbum,albumLength);
	}

	public AlbumFragment(){
		super();
	}

	@Override
	protected void startIntentFromListViewElement(Info info) {
		Intent i=new Intent(getActivity(), AlbumActivity.class);
		i.putExtra("info", info);
		startActivity(i);

	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("LIFE", "ALBUM - onActivityCreated");
		if(!isFragmentResumed){
			AsyncHttpClient clientAlbum = new AsyncHttpClient();
			clientAlbum.get(url,getAlbumContestResponseHandler());

			setListShown(false);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("LIFE", "ALBUM - onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.i("LIFE", "ALBUM - start");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("LIFE", "ALBUM - resume");
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("LIFE", "ALBUM - pause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFE", "ALBUM - stop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFE", "ALBUM - destroy");
	}

}

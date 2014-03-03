package it.polimi.metalnews.fragments;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.activity.ContestActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;


public class ContestFragment extends InfoFragment {

	public ContestFragment(String urlContest, int contestLength) {
		super(urlContest, contestLength);
	}

	public ContestFragment(){
		super();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("LIFE", "CONTEST - onActivityCreated");
		if(!isFragmentResumed){
			AsyncHttpClient clientAlbum = new AsyncHttpClient();
			clientAlbum.get(url,getAlbumContestResponseHandler());

			setListShown(false);
		}else {
			
			setInfoAdapter();
		}
	}

	@Override
	protected void startIntentFromListViewElement(Info info) {
		Intent i=new Intent(getActivity(), ContestActivity.class);
		i.putExtra("info", info);
		startActivity(i);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("LIFE", "CONTEST - onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);

	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.i("LIFE", "CONTEST - start");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("LIFE", "CONTEST - resume");
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("LIFE", "CONTEST - pause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFE", "CONTEST - stop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFE", "CONTEST - destroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("LIFE", "CONTEST - destroyView");
	}

}
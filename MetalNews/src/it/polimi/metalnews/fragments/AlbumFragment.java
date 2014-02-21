package it.polimi.metalnews.fragments;

import it.polimi.metalnews.AlbumActivity;
import android.content.Intent;




public class AlbumFragment extends InfoFragment {


	protected static final int ALBUM_CONTEST_LENGTH = 20;
	
	
	public AlbumFragment(String url) {
		super(url);
	}


	@Override
	protected void startIntentFromListViewElement(String response) {
		Intent i=new Intent(getActivity(), AlbumActivity.class);
		i.putExtra("albumHtml", response);
		startActivity(i);
		
	}
	

}

package it.polimi.metalnews.activity;

import it.polimi.metalnews.Info;
import it.polimi.metalnews.R;
import it.polimi.metalnews.SettingActivity;
import it.polimi.metalnews.fragments.AlbumFragment;
import it.polimi.metalnews.fragments.ContestFragment;
import it.polimi.metalnews.fragments.InfoFragment;
import it.polimi.metalnews.fragments.NewsFragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

public class HomeActivity extends FragmentActivity implements
ActionBar.TabListener {

	private static final int NEWS_LENGTH = 35;
	private static final int ALBUM_CONTEST_LENGTH = 20;
	private static final String URL_CONTEST = "http://metalitalia.com/category/contest/";
	private static final String URL_NEWS = "http://metalitalia.com/category/notizie/";
	private static final String URL_ALBUM = "http://metalitalia.com/category/album/";
	private static final int RESULT_SETTINGS = 1;

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	private Info[] news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("LIFE", "HOME - create");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);


		setContentView(R.layout.activity_home);


		//		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.setTitle(R.string.app_name);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}
	

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.i("LIFE", "HOME - start");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.i("LIFE", "HOME - resume");
	}


	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.i("LIFE", "HOME - pause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFE", "HOME - stop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFE", "HOME - destroy");
	}
	
	@Override
	public void onBackPressed() {
	    
	}
	
	public Info[] getNewsFromIntent(String tag, int n) {

		Info[] news = new Info[n];
		Parcelable[] newsParc = getIntent().getExtras().getParcelableArray(tag);

		int cont=0;
		for(Parcelable x : newsParc){

			news[cont] = (Info) x;
			cont++;
		}

		return news;
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.refresh:
	            refresh();
	            return true;
	        
	        case R.id.menu_settings:
	            Intent i = new Intent(this, SettingActivity.class);
	            startActivityForResult(i, RESULT_SETTINGS);
	            return true;
	       
	    }
		return false;
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
           
            break;
 
        }
 
    }
	
	private void refresh() {
		
		int currentIndex = mViewPager.getCurrentItem();
		InfoFragment infoFragment = ((SectionsPagerAdapter)mViewPager.getAdapter()).getFragment(currentIndex);
		infoFragment.refreshList();
		
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private static final int NUMBER_OF_FRAGMENT = 3;
		
		private Map<Integer, InfoFragment> mPageReferenceMap = new HashMap<Integer, InfoFragment>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public ListFragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			ListFragment fragment=null;

			
			
			switch(position)
			{

			case 0:

				fragment=new NewsFragment(getNewsFromIntent("news", NEWS_LENGTH ),URL_NEWS, NEWS_LENGTH);
				mPageReferenceMap.put(Integer.valueOf(position), (InfoFragment) fragment);
			
				break;
			case 1:
				fragment=new AlbumFragment(URL_ALBUM,ALBUM_CONTEST_LENGTH);
				mPageReferenceMap.put(Integer.valueOf(position), (InfoFragment) fragment);
				
				break;
			case 2:
				fragment=new ContestFragment(URL_CONTEST,ALBUM_CONTEST_LENGTH);
				mPageReferenceMap.put(Integer.valueOf(position), (InfoFragment) fragment);
				
				break;
			}

			return fragment;
		}

		public InfoFragment getFragment(int key) {
			
			return mPageReferenceMap.get(key);
		}
		
		@Override
		public int getCount() {

			return NUMBER_OF_FRAGMENT;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_1).toUpperCase(l);
			case 1:
				return getString(R.string.title_2).toUpperCase(l);
			case 2:
				return getString(R.string.title_3).toUpperCase(l);
			}
			return null;
		}
	}

}

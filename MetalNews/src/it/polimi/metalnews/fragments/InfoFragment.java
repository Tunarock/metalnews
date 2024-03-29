package it.polimi.metalnews.fragments;

import java.io.OutputStreamWriter;

import it.polimi.metalnews.ImageDownloader;
import it.polimi.metalnews.ImageDownloaderNews;
import it.polimi.metalnews.Info;
import it.polimi.metalnews.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public abstract class InfoFragment extends ListFragment {

	
	protected Info[] info;
	protected Info[] moreInfo;
	protected String url;
	protected int page;
	protected int size;
	protected static final String PAGE_SUFFIX = "page/";
	protected boolean isFragmentResumed;
	protected static final int ID_NOTIFICATION = 1;
	private int lastVisiblePosition;
	private View progressbar;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null)
			resumeState(savedInstanceState);
		
		progressbar = getActivity().getLayoutInflater().inflate(R.layout.fragment_progressbar, null);

	}
	
	private void resumeState(Bundle savedInstanceState) {
		
		info = getInfoFromParcelable(savedInstanceState.getParcelableArray("info"));
		moreInfo = getInfoFromParcelable(savedInstanceState.getParcelableArray("moreInfo"));
		url = savedInstanceState.getString("url");
		size = savedInstanceState.getInt("size");
		page = savedInstanceState.getInt("page");
		
	}
	
	private Info[] getInfoFromParcelable(Parcelable[] newsParc) {

		if (newsParc!=null) {
			
			Info[] info = new Info[newsParc.length];
			
			int cont = 0;
			for (Parcelable x : newsParc) {

				info[cont] = (Info) x;
				cont++;
			}
			return info;
		}
		
		return null;
	}	

	@Override
	public void onSaveInstanceState (Bundle outState){
		super.onSaveInstanceState(outState);
		
		Log.i("LIFE", "INFO - saved");
		
		outState.putParcelableArray("info", info);
		outState.putParcelableArray("moreInfo", moreInfo);
		outState.putInt("page", page);
		outState.putInt("size", size);
		outState.putString("url", url);
	}

	public InfoFragment(String url,int size) {
		super();
		this.url=url;
		this.size=size;
		page = 1;
		isFragmentResumed=false;
	}

	public InfoFragment(){
		isFragmentResumed=false;
		page = 1;
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isFragmentResumed = true;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isFragmentResumed = false;
	}

	protected AsyncHttpResponseHandler getAlbumContestResponseHandler(){

		return new AsyncHttpResponseHandler() {			


			@Override
			public void onSuccess(String response) {


				info=getArrayInfoFromHtml(response);
				Log.i("LIFE", "INFO - success");
				setInfoAdapter();

				setListShown(true);

			}


			@Override
			public void onFailure(int statusCode,
					org.apache.http.Header[] headers,
					byte[] responseBody,
					java.lang.Throwable error) {

				//DISPLAY A NOTIFICATION OR A BUTTON TO SAY THAT MAYBE WIRELESS IS NOT ACTIVATED

			}
		};
	}


	protected void setInfoAdapter() {

		View view = getActivity().getLayoutInflater().inflate(R.layout.more_content, null);
		//	    TextView textinlfated = (TextView) view.findViewById(R.id.more_content);
		ListView lv = getListView();
		if(lv.getFooterViewsCount()==0){
			lv.addFooterView(view);
		}
		NewsAdapter ada=new NewsAdapter(getActivity().getBaseContext(), info);
		setListAdapter(ada);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				if(position<info.length){
					startIntentFromListViewElement(info[position]);
				}else{
					
					ListView lv = getListView();
					lv.removeFooterView(view);
					
					lv.addFooterView(progressbar);
					getMoreContent();
					lastVisiblePosition = lv.getFirstVisiblePosition();
				}
			}

			
		});
	}

	private void getMoreContent() {
		
		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.get(url+PAGE_SUFFIX+String.valueOf(++page),new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String response) {

				moreInfo = getArrayInfoFromHtml(response);
				mergeInfos();
				ListView lv = getListView();
				lv.removeFooterView(progressbar);
				setInfoAdapter();
				lv.setSelection(lastVisiblePosition);
			}
			
		});
	}
	
	private void mergeInfos(){
		
		int total_size = info.length+moreInfo.length;
		
		Info[] newInfo = new Info[total_size];
		
		int i;
		for(i=0;i<info.length;i++){
			newInfo[i]=info[i];
		}
		
		for(int j=0;j<moreInfo.length;i++,j++){
			newInfo[i]=moreInfo[j];
		}
		
		info=newInfo;
		
	}

	protected abstract void startIntentFromListViewElement(Info info);

	protected class NewsAdapter extends ArrayAdapter<Info> {
		private final Context context;
		private Info[] values;
		
		protected void setValues(Info[] info){
			
			values=info;
			notifyDataSetChanged();
		}

		public NewsAdapter(Context context, Info[] values) {
			super(context, R.layout.fragment_info, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.fragment_info, parent, false);


			TextView backtitle = (TextView) rowView.findViewById(R.id.news_backtitle);
			TextView title = (TextView) rowView.findViewById(R.id.news_title);
			ImageView img = (ImageView) rowView.findViewById(R.id.news_img);


			backtitle.setText(values[position].getBacktitle());
			title.setText(values[position].getTitle());
			if(values[position].getImgUrl() != null)
				if(values[position].getImg() == null)
				{
					ImageDownloader imgDown= new ImageDownloaderNews(img, values, position);
					imgDown.execute(values[position].getImgUrl());
				}
				else
					img.setImageBitmap(values[position].getImg());

			return rowView;
		}
		
		

	}

	protected Info[] createInfoBundle(int n, String[] titles, String[] targetUrl, String[] imageUrls){

		Info[] info = new Info[n];

		for(int cont=0;cont<n;cont++)
		{			
			info[cont]=new Info(titles[cont],imageUrls[cont], targetUrl[cont]);	
		}

		return info;
	}

	protected Info[] getArrayInfoFromHtml(String response) {


		String[] imageUrls= new String[size];	
		String[] titles = new String[size];
		String[] targetUrl = new String[size];

		Document doc=Jsoup.parse(response);
		Element link = doc.getElementById("recent-posts");

		Elements blocks= link.getElementsByClass("entry");

		int i=0;
		for(Element block: blocks)
		{
			titles[i]=block.getElementsByClass("title").get(0).text();
			targetUrl[i]=block.getElementsByClass("title").get(0).getElementsByTag("a").get(0).attr("href");
			Elements elementsByTag = block.getElementsByTag("img");
			if(elementsByTag.size()>0)
				imageUrls[i]=elementsByTag.get(0).attr("src");					

			i++;
		}

		Log.i("getdata", "getdata");

		return createInfoBundle(size, titles,targetUrl,imageUrls);
	}

	public void refreshList() {
		
		AsyncHttpClient clientAlbum = new AsyncHttpClient();
		clientAlbum.get(url,new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String response) {

				Info[] newInfo = getArrayInfoFromHtml(response);
				if(newInfo[0].getTargetUrl().compareTo(info[0].getTargetUrl())!=0){
					info=newInfo;
					
					try {
						 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("settings.txt", Context.MODE_PRIVATE));
					     outputStreamWriter.write(info[0].getTitle());
					     outputStreamWriter.close();
					} catch (Exception e) {
					  e.printStackTrace();
					}
					
					NotificationManager mNotificationManager =
						    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
						mNotificationManager.cancel(ID_NOTIFICATION);
					
					
					
					setInfoAdapter();
				}
					
			}
			
		});
		
	}

}

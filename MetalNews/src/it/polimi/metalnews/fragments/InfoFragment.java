package it.polimi.metalnews.fragments;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.polimi.metalnews.AlbumActivity;
import it.polimi.metalnews.ImageDownloader;
import it.polimi.metalnews.ImageDownloaderNews;
import it.polimi.metalnews.Info;
import it.polimi.metalnews.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


public abstract class InfoFragment extends ListFragment {

	protected static final int ALBUM_CONTEST_LENGTH = 20;
	protected Info[] info;
	protected String url;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);



	}

	public InfoFragment(String url) {
		super();
		this.url=url;
	}

	public InfoFragment(){
		super();
	}

	protected AsyncHttpResponseHandler getAlbumContestResponseHandler(){

		return new AsyncHttpResponseHandler() {			


			@Override
			public void onSuccess(String response) {


				info=getArrayInfoFromHtml(response);

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
		NewsAdapter ada=new NewsAdapter(getActivity().getBaseContext(), info);
		setListAdapter(ada);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub

				AsyncHttpClient client = new AsyncHttpClient();
				String url=info[position].getTargetUrl();

				client.get(url,new AsyncHttpResponseHandler() {

					public void onStart(){

					}

					@Override
					public void onSuccess(String response) {


						startIntentFromListViewElement(response);

					}



					@Override
					public void onFailure(int statusCode,
							org.apache.http.Header[] headers,
							byte[] responseBody,
							java.lang.Throwable error) {

					}
				});


			}

		});
	}


	protected abstract void startIntentFromListViewElement(String response);

	protected class NewsAdapter extends ArrayAdapter<Info> {
		private final Context context;
		private final Info[] values;

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

	private Info[] createInfoBundle(int n, String[] titles, String[] targetUrl, String[] imageUrls){

		Info[] info = new Info[n];

		for(int cont=0;cont<n;cont++)
		{			
			info[cont]=new Info(titles[cont],imageUrls[cont], targetUrl[cont]);	
		}

		return info;
	}

	protected Info[] getArrayInfoFromHtml(String response) {


		String[] imageUrls= new String[ALBUM_CONTEST_LENGTH];	
		String[] titles = new String[ALBUM_CONTEST_LENGTH];
		String[] targetUrl = new String[ALBUM_CONTEST_LENGTH];

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

		return createInfoBundle(ALBUM_CONTEST_LENGTH, titles,targetUrl,imageUrls);
	}



}

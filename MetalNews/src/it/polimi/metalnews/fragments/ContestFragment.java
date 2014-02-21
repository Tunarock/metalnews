package it.polimi.metalnews.fragments;
import it.polimi.metalnews.ContestActivity;
import it.polimi.metalnews.ImageDownloader;
import it.polimi.metalnews.ImageDownloaderNews;
import it.polimi.metalnews.Info;
import it.polimi.metalnews.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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

public class ContestFragment extends ListFragment {

	private Info[] contest;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		NewsAdapter ada=new NewsAdapter(getActivity().getBaseContext(), contest);
		setListAdapter(ada);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				
				AsyncHttpClient client = new AsyncHttpClient();
				String url=contest[position].getTargetUrl();

					client.get(url,new AsyncHttpResponseHandler() {
						
						public void onStart(){
						
						}
						
						@Override
						public void onSuccess(String response) {
							
							
						Intent i=new Intent(getActivity(), ContestActivity.class);
						i.putExtra("contestHtml", response);
						startActivity(i);
						
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
	
	public ContestFragment(Info[] contest)
	{
		super();
		this.contest=contest;

	}

	
		class NewsAdapter extends ArrayAdapter<Info> {
			private final Context context;
			private final Info[] values;

			public NewsAdapter(Context context, Info[] values) {
				super(context, R.layout.fragment_news, values);
				this.context = context;
				this.values = values;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View rowView = inflater.inflate(R.layout.fragment_news, parent, false);


				TextView backtitle = (TextView) rowView.findViewById(R.id.news_backtitle);
				TextView title = (TextView) rowView.findViewById(R.id.news_title);
				ImageView img = (ImageView) rowView.findViewById(R.id.news_img);
				
						
				backtitle.setText(values[position].getBacktitle());
				title.setText(values[position].getTitle());
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

}

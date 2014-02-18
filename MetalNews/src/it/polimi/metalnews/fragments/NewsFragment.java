package it.polimi.metalnews.fragments;

import it.polimi.metalnews.ImageDownloader;
import it.polimi.metalnews.News;
import it.polimi.metalnews.R;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;

public class NewsFragment extends ListFragment {

	//	@Override
	//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	//			Bundle savedInstanceState) {			
	//		return inflater.inflate(R.layout.activity_news, container, false);
	//	}

	private News[] news;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		NewsAdapter ada=new NewsAdapter(getActivity().getBaseContext(), news);
		setListAdapter(ada);

	}

	public NewsFragment(News[] news)
	{
		super();
		this.news=news;

	}

	
		class NewsAdapter extends ArrayAdapter<News> {
			private final Context context;
			private final News[] values;

			public NewsAdapter(Context context, News[] values) {
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
					ImageDownloader imgDown= new ImageDownloader(img, values, position);
					imgDown.execute(values[position].getImgUrl());
					
				}
				else
					img.setImageBitmap(values[position].getImg());
					
				return rowView;
			}

		}
	}

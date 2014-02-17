package it.polimi.metalnews.fragments;

import it.polimi.metalnews.News;
import it.polimi.metalnews.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		return inflater.inflate(R.layout.fragment_news, container, false);
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
			TextView title = (TextView) rowView.findViewById(R.id.news_backtitle);
			ImageView img = (ImageView) rowView.findViewById(R.id.news_img);
			
			backtitle.setText(values[position].getBacktitle());
			title.setText(values[position].getTitle());
//			img.set			
			return rowView;
		}

	}
}

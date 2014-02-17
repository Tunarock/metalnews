package it.polimi.metalnews.fragments;

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
	
	class MySimpleArrayAdapter extends ArrayAdapter<String> {
		  private final Context context;
		  private final String[] values;

		  public MySimpleArrayAdapter(Context context, String[] values) {
		    super(context, R.layout.fragment_news, values);
		    this.context = context;
		    this.values = values;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.fragment_news, parent, false);
		    TextView textView = (TextView) rowView.findViewById(R.id.label);
		    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		    textView.setText(values[position]);
		    // change the icon for Windows and iPhone
		    String s = values[position];
		    if (s.startsWith("iPhone")) {
		      imageView.setImageResource(R.drawable.no);
		    } else {
		      imageView.setImageResource(R.drawable.ok);
		    }

		    return rowView;
		  }
	
}

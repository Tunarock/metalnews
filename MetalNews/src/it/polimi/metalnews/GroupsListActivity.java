package it.polimi.metalnews;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupsListActivity extends ListActivity {
	
	private final String FILE="listaGruppi.txt";
	private ArrayList<String> groups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		groups=readFile(FILE);
		
		
		
		View header = getLayoutInflater().inflate(R.layout.preference_new_group, null);
		header.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		getListView().addHeaderView(header);
		setListAdapter(new GroupAdapter(getBaseContext(), groups));
		
	}
	
	
	public ArrayList<String> readFile(String file)
	{
		ArrayList<String> groups=new ArrayList<String>();
		
		try {
	        InputStream inputStream = openFileInput(file);

	        if ( inputStream != null ) {
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            String receiveString = "";

	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                groups.add(receiveString);
	            }

	            inputStream.close();
	        }
	    }
	    catch (FileNotFoundException e) {
	      
	    } catch (IOException e) {

	    }
		
		return groups;

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.groups_list, menu);
		return true;
	}
	
	
	protected class GroupAdapter extends ArrayAdapter<String> {
		private final Context context;
		private ArrayList<String> values;
		
		
		
		
		protected void setValues(ArrayList<String>  info){
			values=info;
			notifyDataSetChanged();
		}

		public GroupAdapter(Context context, ArrayList<String>  values) {
			super(context, R.layout.fragment_info, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.preference_groups_row, parent, false);

			TextView name = (TextView) rowView.findViewById(R.id.group_name);
			
			name.setText(groups.get(position));
			

			return rowView;
		}
		
		

	}

}

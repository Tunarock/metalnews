package it.polimi.metalnews;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class GroupsListActivity extends ListActivity {

	private final String FILE="listaGruppi.txt";
	private ArrayList<String> groups;
	private View header;

	Editable groupName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		groups=readFile(FILE);


		header = getLayoutInflater().inflate(R.layout.preference_new_group, null);
		header.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText input = new EditText(GroupsListActivity.this);

				new AlertDialog.Builder(GroupsListActivity.this)
				.setTitle("Nuovo gruppo")
				.setMessage("Inserisci il nome del gruppo")
				.setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						groupName = input.getText(); 

						writeAppendFile(groupName.toString(), FILE);
						GroupAdapter adapter=(GroupAdapter) GroupsListActivity.this.getListAdapter();
						adapter.insert(groupName.toString(), adapter.getCount());
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
				
					}
				}).show();



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

	public void writeAppendFile(String data, String file)
	{
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, Context.MODE_APPEND));
			outputStreamWriter.write(data+"\n");
			outputStreamWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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

			final int f=position;
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.preference_groups_row, parent, false);

			TextView name = (TextView) rowView.findViewById(R.id.group_name);
			name.setText(groups.get(position));


			ImageButton delete= (ImageButton) rowView.findViewById(R.id.cancel);
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					GroupAdapter adapter=(GroupAdapter) GroupsListActivity.this.getListAdapter();

					adapter.remove(groups.get(f));
					removeFromFile(FILE, f);
					adapter.notifyDataSetChanged();

				}
			});


			return rowView;
		}

		protected void removeFromFile(String file, int position) {
			// TODO Auto-generated method stub


			ArrayList<String> readed= readFile(FILE);
			
			assert position >= 0 && position <= readed.size() - 1;
			readed.remove(position);

			
			try {
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));
				
		
			for(final String line : readed)
				outputStreamWriter.write(line+"\n");
			
			outputStreamWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}



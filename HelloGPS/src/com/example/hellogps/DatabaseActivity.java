package com.example.hellogps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity  extends Activity implements Serializable {
	public static String FILENAME = "bazaDanych.txt";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1725782058637571418L;
	public DatabaseActivity DA=this;
	private ListView mainListView;
	private Field[] itemss;
	private ArrayAdapter<Field> listAdapter;
	ArrayList<String> checked = new ArrayList<String>();
	TextView tt;

	/** Called when the activity is first created. */

	ArrayList<Field> FieldsBase;
	int pos_;
	@SuppressWarnings("deprecation")	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.database_activity);

		tt = (TextView) findViewById(R.id.textView11);
		FieldsBase = MainActivity.FieldsBase;
		// Find the ListView resource.
		mainListView = (ListView) findViewById(R.id.mainListView);

		
		mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View item,
					int position, long id) {
				Field field = listAdapter.getItem(position);
				field.toggleChecked();
				SelectViewHolder viewHolder = (SelectViewHolder) item.getTag();
				viewHolder.getCheckBox().setChecked(field.isChecked());
				

				boolean zaznaczenie = listAdapter.getItem(position).checked;
				MainActivity.FieldsBase.get(position).checked=zaznaczenie;
				//tt.setText(position+"zaznaczenie: "+zaznaczenie);
				//listAdapter.getItem(position).checkedO
				

			}
		});

		mainListView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View item,
					int position, long id) {
				// TODO wyskakuje okienko: Edytuj obszar, Usun obszar
				pos_=position;
				AlertDialog.Builder alert = new AlertDialog.Builder(DatabaseActivity.this);

				alert.setTitle("Opcje obszaru");
				alert.setMessage("Wybierz:");

				// Set an EditText view to get user input 
			
////////////////////////////////////////////////	edycja	//////////////////////////////////////////////////////////////
				alert.setPositiveButton("Edytuj nazwe", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				 
					AlertDialog.Builder alert1 = new AlertDialog.Builder(DatabaseActivity.this);

					
					alert1.setMessage("Wpisz nowa nazwe:");
					final EditText et = new EditText(DA);
					et.setText(FieldsBase.get(pos_).name);
					alert1.setView(et);
					alert1.setPositiveButton("Zmien", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  String value;
						  value= et.getText().toString();
						  // Do something with value!
						  
						



							Toast.makeText(getApplicationContext(), "zmieniono nazwe obszaru: "+FieldsBase.get(pos_).name+"=>"+value,
									Toast.LENGTH_SHORT).show();
							  
							FieldsBase.get(pos_).changeName(value);
						
						  MainActivity.mapView.invalidate();
						  
						  }
						});

					alert1.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
							  return;
						  }
						});
				alert1.show();
				  
				  }
				});
///////////////////////////////		usuwanie		///////////////////////////////////////////////
				alert.setNegativeButton("Usun", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
					  
					  String name = FieldsBase.get(pos_).name;
						
						
						FieldsBase.remove(pos_);
						Toast.makeText(MainActivity.MA, "Usunieto obszar: "+name, Toast.LENGTH_SHORT).show();
						FieldsBase = MainActivity.FieldsBase;
						// Set our custom array adapter as the ListView's adapter.
						
						listAdapter = new SelectArralAdapter(DatabaseActivity.this, FieldsBase);
						mainListView.setAdapter(listAdapter);
					  return;
				  }
				});
				alert.setNeutralButton("Wycentruj na mapie", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
					 
					    coord punkt2 = MainActivity.FieldsBase.get(pos_).punkty.get(0);
						MainActivity.mapController.setCenter(punkt2.toGeoPoint() );
						MainActivity.mapController.animateTo(punkt2.toGeoPoint());
						MainActivity.mapController.setZoom(17); 
						MainActivity.mapView.invalidate();
						
						finish();
					
					 
					  return;
				  }
				});
				alert.show();
				
				
				return true;
			}
		});


		// Create and populate planets.
		itemss = (Field[]) getLastNonConfigurationInstance();


		//	ArrayList<Field>  

		if(MainActivity.FieldsBase.size()<1){
			//FieldsBase.add(new Field());
			tt.setText("Brak obszarow w bazie");
			return;
		}



		// Set our custom array adapter as the ListView's adapter.
		listAdapter = new SelectArralAdapter(this, FieldsBase);
		mainListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		menu.add(0, 2, Menu.NONE, "Zapisz baze");
		menu.add(0, 1, Menu.NONE, "Wczytaj baze");
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub



		switch (item.getItemId()) {
		case 1:
			readDataBase();
			listAdapter = new SelectArralAdapter(DatabaseActivity.this, FieldsBase);
			mainListView.setAdapter(listAdapter);
			break;
		case 2:

			saveDataBase();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	/** Holds child views for one row. */
	private static class SelectViewHolder {
		private CheckBox checkBox;
		private TextView textView;

		public SelectViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}

		public CheckBox getCheckBox() {
			return checkBox;
		}

		public TextView getTextView() {
			return textView;

		}
	}

	/** Custom adapter for displaying an array of Planet objects. */
	private static class SelectArralAdapter extends ArrayAdapter<Field> {
		private LayoutInflater inflater;

		public SelectArralAdapter(Context context, ArrayList<Field> Fieldbase) {
			super(context, R.layout.simplerow, R.id.rowTextView, Fieldbase);
			// Cache the LayoutInflate to avoid asking for a new one each time.
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Field to display
			Field field = (Field) this.getItem(position);

			// The child views in each row.
			CheckBox checkBox;
			TextView textView;

			// Create a new row view
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.simplerow, null);

				// Find the child views.
				textView = (TextView) convertView
				.findViewById(R.id.rowTextView);
				checkBox = (CheckBox) convertView.findViewById(R.id.CheckBox01);
				// Optimization: Tag the row with it's child views, so we don't
				// have to
				// call findViewById() later when we reuse the row.
				convertView.setTag(new SelectViewHolder(textView, checkBox));
				// If CheckBox is toggled, update the field it is tagged with.
				checkBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Field planet = (Field) cb.getTag();
						planet.setChecked(cb.isChecked());
					}
				});
			}
			// Reuse existing row view
			else {
				// Because we use a ViewHolder, we avoid having to call
				// findViewById().
				SelectViewHolder viewHolder = (SelectViewHolder) convertView
				.getTag();
				checkBox = viewHolder.getCheckBox();
				textView = viewHolder.getTextView();
			}

			// Tag the CheckBox with the Field it is displaying, so that we can
			// access the planet in onClick() when the CheckBox is toggled.
			checkBox.setTag(field);
			// Display field data
			checkBox.setChecked(field.isChecked());
			textView.setText(field.getName());
			return convertView;
		}
	}

	public Object onRetainNonConfigurationInstance() {
		return itemss;
	}
	public  void saveDataBase() {
		
		File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);

			oos.writeObject(MainActivity.FieldsBase);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(DA.getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(DA.getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		}
		finally{
			try {
				if (oos != null) 
					oos.close();
			} catch (IOException e) { e.printStackTrace();}
			try {
				if (fos != null) 
					fos.close();
			} catch (IOException e) { e.printStackTrace();}
		}

	}
	@SuppressWarnings("unchecked")
	public  void readDataBase() {

		File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			MainActivity.FieldsBase.clear();
			//FieldsBase.addAll((ArrayList<Field>) ois.readObject());
			MainActivity.FieldsBase=(ArrayList<Field>) ois.readObject();
			FieldsBase=MainActivity.FieldsBase;
			Field.ilosc+=MainActivity.FieldsBase.size();
			MainActivity.mapView.invalidate();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(DA.getApplicationContext(), "Nie znaleziono pliku!",Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(DA.getApplicationContext(), "Blad odczytu bazy! "+e,Toast.LENGTH_SHORT).show();

			// TODO Auto-generated catch blocks
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Toast.makeText(DA.getApplicationContext(), "ClassNotFound!",Toast.LENGTH_SHORT).show();

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				if (ois != null) 
					ois.close();
			} catch (IOException e) {e.printStackTrace();}
			try {
				if (fis != null) 
					fis.close();
			} catch (IOException e) {e.printStackTrace();}
			//pobieranie nakladek
			MyLocationListener.mapOverlays= MainActivity.mapView.getOverlays();

			//czyszczenie wczesniej dodanych nakladek(Overlays)
			MainActivity.mapView.getOverlays().clear();
			((MyLocationListener) MainActivity.locationListener).drawFieldBase();
			MainActivity.mapView.invalidate();
		}	
	}
}

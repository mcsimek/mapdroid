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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;




public class MainActivity extends MapActivity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	public static ArrayList<Field> FieldsBase = new ArrayList<Field>();
	public LocationManager lm;
	public static MyLocationListener locationListener;
	public static TextView tx;
	//public static  Context con;
	public static TextView wynik;
	public static TextView obwod;
	public static  MapController mapController;
	public static MapView mapView ;
	public static Drawable drawable;
	public static ToggleButton sledzenieButton;
	public static boolean sledzenie=true;
	public static Drawable android;
	public static MainActivity MA;
	public static Button centruj;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();


		centruj = (Button)findViewById(R.id.button1);




		sledzenieButton = (ToggleButton) findViewById(R.id.toggleButton1);


		wynik = (TextView)findViewById(R.id.wynik);
		//con = getApplicationContext();
		tx = (TextView) findViewById(R.id.pozycja);
		obwod = (TextView) findViewById(R.id.obwod);

		//---używamy klasy LocationManager w celu odczytu lokacji GPS---
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);    

		locationListener = new MyLocationListener();

		lm.requestLocationUpdates( LocationManager.GPS_PROVIDER,   0,  0, locationListener);      

		drawable = this.getResources().getDrawable(R.drawable.images);
		android = this.getResources().getDrawable(R.drawable.androidmarker);
		MA = this;


		centruj.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(MyLocationListener.loc_ != null){
					coord punkt2 = MyLocationListener.punkt2;
					MainActivity.mapController.setCenter(punkt2.toGeoPoint() );
					MainActivity.mapController.animateTo(punkt2.toGeoPoint());
					MainActivity.mapController.setZoom(17); 
					MainActivity.mapView.invalidate();
				}else{
					Toast.makeText(MA, "Brak poprawnej pozycji",Toast.LENGTH_SHORT).show();
				}
			}
		});

		sledzenieButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((sledzenieButton.isChecked()))			        
					sledzenie=true;			          
				else			        
					sledzenie=false;  


				//((MyLocationListener)locationListener).drawFieldBase();
				mapView.invalidate();

			}
		});



		GeoPoint point = new GeoPoint((int)(50.0626*1e6), (int)(19.9367 * 1e6));

		mapController.setCenter(point );
		mapController.animateTo(point);
		mapController.setZoom(17); 
		mapView.invalidate();



		/**/
	}


	public void saveDataBase() {
		String FILENAME = DatabaseActivity.FILENAME;
		File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);

			oos.writeObject(FieldsBase);


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MA.getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(MA.getApplicationContext(), e.toString(),Toast.LENGTH_SHORT).show();

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
	public void readDataBase() {

		String FILENAME = DatabaseActivity.FILENAME;
		File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

		ObjectInputStream ois = null;
		FileInputStream fis = null;
		try {

			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			FieldsBase.clear();
			//FieldsBase.addAll((ArrayList<Field>) ois.readObject());
			FieldsBase=(ArrayList<Field>) ois.readObject();

			Field.ilosc=FieldsBase.size();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MA.getApplicationContext(), "Nie znaleziono pliku!",Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(MA.getApplicationContext(), "Blad odczytu bazy! "+e,Toast.LENGTH_SHORT).show();

			// TODO Auto-generated catch blocks
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Toast.makeText(MA.getApplicationContext(), "ClassNotFound!",Toast.LENGTH_SHORT).show();

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
			((MyLocationListener) locationListener).drawFieldBase();
			mapView.invalidate();
		}	
	}
	void clearMap(){

		//czyszczenie bazy punktów:
		MyLocationListener.punkty.clear();

		//czyszczenie nakładek mapView
		MainActivity.mapView.getOverlays().clear();



		//validacja mapy
		MainActivity.mapView.invalidate();


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.item1:
			if(MyLocationListener.punkty.size()>=3){

				AlertDialog.Builder alert = new AlertDialog.Builder(this);

				alert.setTitle("Dodawanie obszaru");
				alert.setMessage("Podaj nazwe obszaru");

				// Set an EditText view to get user input 
				final EditText input = new EditText(this);
				input.setText("Obszar nr "+(Field.ilosc+1));
				alert.setView(input);

				alert.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value;
						value= input.getText().toString();
						// Do something with value!
						Field f = new Field(value,MyLocationListener.punkty, MyLocationListener.pole, MyLocationListener.obwod);
						//dodaje obszar
						FieldsBase.add(f);



						Toast.makeText(getApplicationContext(), "Dodano obszar("+f.punkty.size()+" punktow)",
								Toast.LENGTH_SHORT).show();
						//czyszczenie zbioru punkt�w
						MyLocationListener.punkty.clear();


					}
				});

				alert.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						return;
					}
				});

				alert.show();



			}
			else
				Toast.makeText(getApplicationContext(), "Za malo punktow!",
						Toast.LENGTH_SHORT).show();

			break;

		case R.id.item2:
			Toast.makeText(getApplicationContext(), "Usunieto "+FieldsBase.size()+" element�w z bazy",
					Toast.LENGTH_SHORT).show();
			FieldsBase.clear();
			break;

		case R.id.item3:

			saveDataBase();
			break;

		case R.id.item4:	

			readDataBase();
			mapView.invalidate();

			break;

		case R.id.item5:
			Intent dialogIntent = new Intent(getBaseContext(), DatabaseActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			getApplication().startActivity(dialogIntent);
			break;
		case R.id.item6:	
			//if(MyLocationListener.punkty.size()>=1)
			clearMap();
			Toast.makeText(getApplicationContext(),"wyczyszczono dane o przebytej drodze",Toast.LENGTH_SHORT).show();

			break;
		case R.id.item7:	

			finish();
			System.exit(0);
			return false;


		}

		return true;
	}





}

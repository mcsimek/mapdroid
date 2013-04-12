package com.example.hellogps;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


public class MyLocationListener implements LocationListener {

	static Projection projection;  
	public static MyOverlay myoverlay;
	public static ArrayList<coord> punkty=new ArrayList<coord>();
	coord punkt1 = new coord();
	public static coord punkt2= new coord();
	public static double pole,obwod;
	LinearLayout linearlayout;
	static List<Overlay> mapOverlays=null;
	public static Location loc_=null;
	
	
	public void onLocationChanged(Location loc) {

		loc_=loc;
		//pobieranie nakladek
		mapOverlays= MainActivity.mapView.getOverlays();

		//czyszczenie wczesniej dodanych nakladek(Overlays)
		MainActivity.mapView.getOverlays().clear();


		if (loc != null   ) {
			punkt1 = punkt2;
			punkt2 = new coord(loc.getLatitude(),loc.getLongitude());

			if(punkt2.isDifferenceFrom(punkt1)){


				if(MainActivity.sledzenie){
					punkty.add(punkt2);

					pole = obliczPole();
					obwod = obliczObwod();
					MainActivity.wynik.setText("pole: "+String.format("%.2f", pole)+" m kw.");
					MainActivity.obwod.setText("obwod: "+String.format("%.2f", obwod)+" m.");

				}
				else
					punkty.clear();



				MainActivity.tx.setText(punkty.size()+". "+punkt2);


			}

		} else {

			if (MainActivity.sledzenie) {
				MainActivity.tx.setText("sledzenie OFF");
			} else {

				MainActivity.tx.setText("nie poprawna pozycja");
			}


		}

		drawActualPos();

		if(MainActivity.sledzenie)
			drawActualField();

		drawFieldBase();
		refreshMap();

	}

	public void drawActualPos(){
		// rysowanie kropki <overlayitem> aktualna pozycja				
		OverlayItem overlayitem = new OverlayItem(punkt2.toGeoPoint(), "Komunikat", "To Twoja pozycja");   
		HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(MainActivity.drawable, MainActivity.MA.getApplicationContext());
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}
	public void drawActualField(){

		// rysowanie aktualnego obwodu i powierzchni obszaru 
		MainActivity.mapController =MainActivity.mapView.getController();
		projection =  MainActivity.mapView.getProjection();
		myoverlay = new MyOverlay(punkty,projection);
		mapOverlays.add(myoverlay);      
		// koniec rysowania 
	}
	public void drawFieldBase(){
		HelloItemizedOverlay wskaznikObszaru=null;
		OverlayItem overlayitem = null;
		projection =  MainActivity.mapView.getProjection();

		// rysowanie wczesniej zapisanych obszarow
		for(Field f  : MainActivity.FieldsBase){

			if(f.checked){

				myoverlay = new MyOverlay(f.punkty,projection);

				overlayitem = new OverlayItem(f.punkty.get(0).toGeoPoint(), "Obszar", f.kom());   
				wskaznikObszaru = new HelloItemizedOverlay(MainActivity.android, MainActivity.MA.getApplicationContext());
				wskaznikObszaru.addOverlay(overlayitem);


				mapOverlays.add(myoverlay);
				mapOverlays.add(wskaznikObszaru);
			}
			else
				continue;

		}
	}
	public void refreshMap(){

		MainActivity.mapView.invalidate();
	}
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		

	}

	public double obliczPole(){

		//wynik.setText("Licze!");
		//Thread.sleep(2000);
		if(punkty.size()>=3){
			double suma=0.0;

			for(int i=1 ; i < punkty.size()-1 ; i++){

				suma+= pole(punkty.get(0),punkty.get(i),punkty.get(i+1));

			}

			//Toast.makeText(con, punkty.size()+" punkt�w, pole: "+suma,Toast.LENGTH_SHORT).show();

			return suma;
		}
		return 0;
	}
	public  double obliczObwod(){

		if(punkty.size()>=2){
			double suma=0.0;
			coord t;
			for(int i=0 ; i < punkty.size()-1 ; i++){

				t = punkty.get(i);
				suma+= t.odleglosc(punkty.get(i+1))*111196.672;

			}

			return suma;
		}
		return 0;	

	}

	double pole(coord aa,coord bb,coord cc){

		double stTom = 111196.672;
		double a = aa.odleglosc(bb)*stTom;
		double b = bb.odleglosc(cc)*stTom;
		double c = cc.odleglosc(aa)*stTom;

		double p = 0.5 * (a+b+c);//po�owa obwodu

		double pole_ = Math.sqrt(  p*(p-a)*(p-b)*(p-c) );//pole ze wzoru Herona

		return pole_;
	}


}

package com.example.hellogps;

import java.io.Serializable;

import com.google.android.maps.GeoPoint;

public class coord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double x,y;
	coord(){ 
		x=50;
		y=19.0;
	}
	coord(double xx,double yy){x=xx;y=yy;}

	public String toString(){
		String str = "("+String.format("%.2f", x)+"; "+String.format("%.2f", y)+")";
		
		return str;
	}
	public double odleglosc(coord c){

		double odl;
		odl = Math.sqrt((x-c.x)*(x-c.x)+(y-c.y)*(y-c.y));

		return odl;
	}
	public boolean isDifferenceFrom( coord a){


		if( Math.abs(this.x-a.x) < 0.000001 || Math.abs(this.y-a.y) < 0.000001)
			return false;
		else
			return true;


	}
	public GeoPoint toGeoPoint(){
		GeoPoint gp = new GeoPoint((int)(x*1e6),(int) (y*1e6));
		return gp;
	}
	/*
	public String toSaveString(){
		return "(x:"+x+";y:"+y+"),";
	}*/
	
}

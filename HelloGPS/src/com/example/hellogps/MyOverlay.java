package com.example.hellogps;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MyOverlay extends Overlay implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8487432893105627110L;
	ArrayList<coord> punkty=new ArrayList<coord>();
	Projection projection;
	
	
	public MyOverlay(ArrayList<coord> punkty_,Projection projection_){
		 punkty=punkty_;
		 projection=projection_;
	}

	
	public void draw(Canvas canvas, MapView mapv, boolean shadow){
		super.draw(canvas, mapv, shadow);

		Paint   mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);


		coord punkt_1;
		GeoPoint gP1;
		Point p1;
		Path path= new Path();;
		path.setFillType(Path.FillType.WINDING);




		//	Path.FillType.
		for(int i=0 ; i < punkty.size(); i++){

			punkt_1 = punkty.get(i);
			//punkt_2 = MainActivity.punkty.get(i+1);
			gP1 = punkt_1.toGeoPoint();
			//gP2 = punkt_2.toGeoPoint();

			p1 = new Point();
			//p2 = new Point();
			//path = new Path();

			projection.toPixels(gP1, p1);
			//projection.toPixels(gP2, p2);

			if(i==0)
				path.moveTo(p1.x, p1.y);
			else					
				path.lineTo(p1.x, p1.y);
			//path.lineTo(p2.x,p2.y);

			//canvas.drawPath(path, mPaint);


		}
		path.close();
		mPaint.setAlpha(30);
		canvas.drawPath(path, mPaint);

		mPaint.setColor(Color.CYAN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setAlpha(254);
		canvas.drawPath(path, mPaint);


		
	}




}

package com.example.hellogps;

import java.io.Serializable;
import java.util.ArrayList;

public class Field implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int id;
	static int ilosc;
	ArrayList<coord> punkty ;
	public String name;
	boolean checked;
	String cosTamJeszcze;
	double polePow;
	double obwod;
	coord centrum;
	public String komunikat;
		

	
	public Field(String _name,ArrayList<coord> _punkty,double _polePow,double _obwod){
		ilosc++;
		id=ilosc;
		cosTamJeszcze="cos tam";
		checked = true;
			
		punkty = new ArrayList<coord>();
		for(int i=0 ; i< _punkty.size(); i++)
			punkty.add( _punkty.get(i));

		if(_name==null || _name == "" ||  _name.isEmpty())
			name="Field nr "+id;
		else
			name=_name;
		
		polePow=_polePow;
		centrum = new coord();
		obwod=_obwod;
		
		komunikat = this.kom();
		
	}
	public String kom(){
		return name+"\n"+String.format("%.2f", polePow)+" mkw";	
	}
	public String getName() {
		return name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public void toggleChecked() {
		checked = !checked;
	}
	public void changeName(String _name){
		name=_name;
	}
	public String toString(){

		String s;
		s = id + ". "+name+" pole: "+
				String.format("%.2f", polePow)
				+" obwod: "+ String.format("%.2f", obwod);

		return s;

	}
	
}

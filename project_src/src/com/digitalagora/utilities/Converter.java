package com.digitalagora.utilities;

import java.lang.Math;

public class Converter {

    public Converter()
    {
        //double startLat, startLong, endLat, endLong;
        //startLat = 30.447540034941834;
        //startLong = -84.30104613304138;
    	//
        //endLat = 30.446041643331263;
        //endLong = -84.29810643196106;
    	//
        //double distance = computeLatLongDistance( startLat, startLong, endLat, endLong );
    }

    public static double computeLatLongDistance( double startLat, double startLong, double endLat, double endLong )
    {
        double returnDouble = 0;
       

    	double dLat= endLat-startLat;
    	double dLon= endLong-startLong;
    	
    	if(dLat<0.0) dLat= 0.0 - dLat;
    	if(dLon<0.0) dLon= 0.0 - dLon;
    	
    	double d= (dLat) + (dLon);
    	
    	//this should be scaled and square-rooted, but WHY, just a waste of battery ;-)
    	
    	returnDouble= d;
    	
        //System.out.println("Returning distance: " + returnDouble + " meters");
        return returnDouble;
    }
   
    public static void main(String args[])
    {
        Converter c = new Converter();
    }
}


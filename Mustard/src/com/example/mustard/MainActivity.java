package com.example.mustard;

import org.wikispeedia.speedlimit.SpeedlimitListener;
import org.wikispeedia.speedlimit.SpeedlimitManager;

import com.example.mustard.R;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	float mySpeed = -1;
	double myLat;
	double myLong;
	int myLimit;
	float myBearing;
	String limitContrib = "";
	String msg = "TEST";
	
	String limitPath="speed30";
	
	static String myName = "Adam w";
	static String myEmail = "awilhelm@oswego.edu";
	
	
	
	static SpeedlimitManager slm;
	static ImageView spdlmt;
	TextView txt;
	Button btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        spdlmt = (ImageView) findViewById(R.id.spdlmt);
        spdlmt.setImageResource(R.drawable.speed30);
        
        
        txt = (TextView)findViewById(R.id.textView1);
        txt.setText(msg);
        
        btn = (Button)findViewById(R.id.signSub);
        
        //Listening to button event
        btn.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), SignSubmitter.class);
 
                startActivity(nextScreen);
 
            }//end onClick
        });//end new onclick listener        
        
     
        
        //Instantiate a location manager 
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Add a location listener to notify of location changes
        LocationListener locationListener = new LocationListener() {
        	//When the location changes...
            public void onLocationChanged(Location location) {
            //Everything below is called when/if the location changes

            	//This method handles all the logic for
            	//"what do I do if the location changes??"
            	locUpdates(location, txt);

            }//end onLocationChanged
            
            //The following three methods exist here solely due to the
            //implementing of the interface LocationListener
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };//end LocationListener instantiation

        // Register the listener with the Location Manager to receive location updates
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        
        //Instantiate a new manager for the wikispeedia database
        slm= new SpeedlimitManager(getBaseContext());
        
        //Create a listener to notify of speedlimit changes
        SpeedlimitListener speedlimitListener = new SpeedlimitListener() {
        	@Override
	        public void onSpeedLimitChanged(Integer speedlimit, String copyright) {
        		//Everything below is called if there is a new sign returned
        		myLimit = speedlimit;
        		limitContrib = copyright;
        		setLimitPath();
        		
        	}
        };  
	  	slm.requestChanges( speedlimitListener );
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }//end onCreateOptionsMenu

    public void locUpdates(Location loc, TextView txt) {
    	//if (loc.hasSpeed()) {
    		mySpeed = loc.getSpeed();
    		
    	//}//end if
    	if (loc.hasBearing()) {
    		myBearing = loc.getBearing();
    	}
    	
    	myLong = loc.getLongitude();
		myLat = loc.getLatitude();
		
        msg="Location has been changed!\n" + myLong + 
       		 "\n" + myLat;
        msg+= "\n\nYour speed: " + mySpeed;
        
        msg+="\n\nSpeed limit: " + myLimit;
        
        msg+="\n\nBearing: " + myBearing;
        
        msg+="\n\nSpeedlimit contributor: "+ limitContrib;
		
        txt.setText(msg);
    	
    }//end locUpdates
    
    public void setLimitPath() {
    	limitPath = "speed" + 30;
    }
    
}//end MainActivity

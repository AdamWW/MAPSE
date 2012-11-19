package edu.mustard.mapse_final;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SignSubmitter extends Activity {

final String toastMessage = "Sign submitted for speed limit of ";
	
	SLMExtender slm;
	Boolean debug = true;
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_sign_submitter);

	        //Button, button, who's got the button?
	        Button bck   = (Button)findViewById(R.id.back);
	        Button spd25 = (Button)findViewById(R.id.spd25);
	        Button spd30 = (Button)findViewById(R.id.spd30);
	        Button spd35 = (Button)findViewById(R.id.spd35);
	        Button spd40 = (Button)findViewById(R.id.spd40);
	        Button spd45 = (Button)findViewById(R.id.spd45);
	        Button spd50 = (Button)findViewById(R.id.spd50);
	        Button spd55 = (Button)findViewById(R.id.spd55);
	        Button spd60 = (Button)findViewById(R.id.spd60);
	        Button spd65 = (Button)findViewById(R.id.spd65);
	        Button spd70 = (Button)findViewById(R.id.spd70);
	        Button spd75 = (Button)findViewById(R.id.spd75);
	        Button spd80 = (Button)findViewById(R.id.spd80);
	        
	        //Try to pull the SLM object out of the parcelable passed in
	        Intent i = getIntent();
	        try {
				slm = (SLMExtender) i.getParcelableExtra("slm");
			} catch (Exception e) {
				Log.e("error: ", e.getMessage());
			}//end try/catch
	        

	        //back button finishes activity and returns to main screen
	        bck.setOnClickListener(new View.OnClickListener() {public void onClick(View arg0) {finish();}});
	         
	        /*******************************************************************
	         * Following button listeners are all for submitting speed limit data
	         * to the Wikispedia database
	         */
	        
	        spd25.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 25, 0);
					}//end if
					Toast.makeText(getApplicationContext(), toastMessage+"25 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd30.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 30, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"30 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd35.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 35, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"35 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd40.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 40, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"40 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd45.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 45, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"45 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd50.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 50, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"50 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd55.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 55, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"55 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd60.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 60, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"60 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd65.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 65, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"65 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd70.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 70, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"70 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd75.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 75, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"75 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });
	        
	        spd80.setOnClickListener(new OnClickListener() {
	        	public void onClick(View arg0) {
	        		if(!debug) {
						slm.submit(slm.getContrib_name(), slm.getContrib_email(), 80, 0);
					}//end if
	        		Toast.makeText(getApplicationContext(), toastMessage+"80 mph", Toast.LENGTH_SHORT).show();
	        	}
	        });

	    }//end onCreate

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // getMenuInflater().inflate(R.menu.activity_sign_submitter, menu);
	        return true;
	    }//end onCreateOptionsMenu

}//end SignSubmitter

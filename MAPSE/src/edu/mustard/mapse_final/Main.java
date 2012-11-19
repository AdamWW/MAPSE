package edu.mustard.mapse_final;

import org.wikispeedia.speedlimit.SpeedlimitListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	
	/*****************************************
	 * Everything I've put in here is essentially just test/sample/example code! 
	 */
	
	SLMExtender slm; // <------Needed!
	WriteLog logger = new WriteLog();
	Button btn;
	Button btn2;
	TextView txt;
	DBManager dbman; // <------Needed!
	public static UserSettings stgs; // <------Needed!
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	
    	/***************
    	 * Mike: This bit is important. This is where we look to see if there
    	 * are already settings in the database, otherwise we instantiate a new
    	 * settings object by showing the splash screen & running settings (first run).
    	 * (the important part is stgs=dbman.getSettings();
    	 * 	as this grabs all the user settings from the DB)
    	 */
    	dbman = new DBManager(this.getApplicationContext());
    	
    	stgs = dbman.getSettings();
    	if(stgs.getFirstRun()==1){//if firstrun is 1, settings has not been run yet
    		showSplash();
    	}
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn = (Button)findViewById(R.id.signSub);
        btn2 = (Button)findViewById(R.id.button1);
        txt = (TextView)findViewById(R.id.txt);
        
        //Instantiate a new manager for the wikispeedia database
        try {
			slm = new SLMExtender(getBaseContext());
			
			//Create a listener to notify of speedlimit changes
			SpeedlimitListener sll = new SpeedlimitListener() {
				@Override
			    public void onSpeedLimitChanged(Integer speedlimit, String copyright) {
					//Everything below is called if there is a new sign returned
				}
			};
			slm.requestChanges( sll );
		} catch (Exception e) {
			logger.appendLog(e.getMessage());
		}
        
        //Listening to button event
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	try {
					//Starting a new Intent
					Intent i = new Intent(getApplicationContext(), SignSubmitter.class);
					i.putExtra("slm", slm);
					startActivity(i);
				} catch (Exception e) {
					txt.setText(e.getMessage());
				}
            }//end onClick
        });//end new onclick listener
        //Listening to button event
        
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	txt.setText(stgs.getUserName());
            }//end onClick
        });//end new onclick listener
        //Listening to button event

    }

    private void showSplash() {
		stgs = new UserSettings();
		Toast.makeText(getBaseContext(), "This is when the splash screen would be displayed.",
				Toast.LENGTH_LONG).show();
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
            	try {
        			//Starting a new Intent
        			Intent i = new Intent(getApplicationContext(), Settings.class);
        			startActivity(i);
        		} catch (Exception q) {
        			Log.e("settings error", q.getMessage());
        		}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }









}//END MAIN!!!

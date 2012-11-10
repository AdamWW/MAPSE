package mustard.mapse;

import org.wikispeedia.speedlimit.SpeedlimitListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
	
	/*****************************************
	 * Everything I've put in here is essentially just test/sample/example code! 
	 */
	
	SLMExtender slm;
	WriteLog logger = new WriteLog();
	Button btn;
	TextView txt;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btn = (Button)findViewById(R.id.signSub);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}

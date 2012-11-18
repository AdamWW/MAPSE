package mustard.mapse;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Settings extends Activity implements OnItemSelectedListener{

	private EditText editName;
	private EditText editEmail;
	private EditText editThresh;
	
	private Button ok;
	private Button cancel;
	private Button viewDisclaimer;
	
	//for DB use
	private DBManager dbman;
	private UserSettings stgs;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        stgs = new UserSettings();
        
        //Spinner for selecting category for places:
        Spinner catspin = (Spinner) findViewById(R.id.cat_spinner);

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, stgs.getCatArr());
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catspin.setAdapter(spinnerArrayAdapter1);
      	catspin.setOnItemSelectedListener(this);
      	
      //Spinner for selecting alarm:
        Spinner alrmspin = (Spinner) findViewById(R.id.alarm_spinner);

        // Application of the Array to the Spinner
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, stgs.getAlarmArr());
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alrmspin.setAdapter(spinnerArrayAdapter2);
        alrmspin.setOnItemSelectedListener(this);
        
		

     

     editName = (EditText)findViewById(R.id.txt_name);
     editEmail = (EditText)findViewById(R.id.txt_email);
     editThresh = (EditText)findViewById(R.id.slct_num);
     
     //The following listen to the edit text elements
     editName.addTextChangedListener(new TextWatcher() {
    	    public void afterTextChanged(Editable s) {
    	         Log.v("LOG","After text change value "+s.toString());
    	    }
    	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    	    	}
    	    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	       stgs.setUserName(s.toString());
    	    }
    });
    editEmail.addTextChangedListener(new TextWatcher() {
 	    public void afterTextChanged(Editable s) {
 	         Log.v("LOG","After text change value "+s.toString());
 	    }
 	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 	    	}
 	    public void onTextChanged(CharSequence s, int start, int before, int count) {
 	    	stgs.setUserEmail(s.toString());
 	    }
 	});
    editThresh.addTextChangedListener(new TextWatcher() {
  	    public void afterTextChanged(Editable s) {
  	         Log.v("LOG","After text change value "+s.toString());
  	    }
  	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  	    	}
  	    public void onTextChanged(CharSequence s, int start, int before, int count) {
  	    	stgs.setAlarmThreshold(Integer.parseInt(s.toString()));
  	    }
  	});
     
     
     //Manager to interact with the database
     dbman = new DBManager (this.getApplicationContext());
     
     
 	ok = (Button)findViewById(R.id.ok);
 	ok.setText("OK");
     
    cancel = (Button)findViewById(R.id.cancel);
    cancel.setText("Cancel");
    
    viewDisclaimer = (Button)findViewById(R.id.viewDisclaimer);
     
    //What happens when right button clicked??
     ok.setOnClickListener(new View.OnClickListener() {
         public void onClick(View arg0) {
         	stgs.setFirstRun(0);
 			try {
 				if(dbman.updateSettings(stgs) <= 0){
 					dbman.addSettings(stgs);
 					Toast.makeText(getBaseContext(), "New settings saved",Toast.LENGTH_SHORT).show(); 
 				}else{
 					Toast.makeText(getBaseContext(), "Settings saved.",Toast.LENGTH_SHORT).show(); 
 				}
 			} catch (Exception e) {
 				Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_LONG).show();
 			}
         }//end onClick
     });//end new onclick listener  
     
   //What happens when left button is clicked??
     cancel.setOnClickListener(new View.OnClickListener() {
         public void onClick(View arg0) {
        	 UserSettings set = dbman.getAllSettings().get(0);
        	 Toast.makeText(getBaseContext(),"Name: " + set.getUserName()
        			 + "\nEmail: " + set.getUserEmail()
        			 + "\nAlarm: " + set.getAlarm()
        			 + "\nCategory: " + set.getCat()
        			 + "\nThreshold: " + set.getAlarmThreshold()
        			 + "\nFirst_Run: " + set.getFirstRun()
        			 ,Toast.LENGTH_LONG).show();
        	 finish();
     }//end onClick
    });//end new onclick listener   
     
    viewDisclaimer.setOnClickListener(new View.OnClickListener() {
         public void onClick(View arg0) {
         	
        	 Toast.makeText(getBaseContext(), R.string.disclaimer,Toast.LENGTH_LONG).show();
         }//end onClick
     });//end new onclick listener

     
     
    }//end oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch(arg0.getId()){
			case R.id.cat_spinner:
	    		stgs.setCat(arg0.getItemAtPosition(arg2).toString());
	    	break;
	        case R.id.alarm_spinner:
	    		stgs.setAlarm(arg0.getItemAtPosition(arg2).toString());
	            break;
	        
		}//end switch
	}


}//END!!!!!!!!!!!

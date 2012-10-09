package com.example.mustard;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignSubmitter extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_submitter);
        
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
        //Listening to button event
        bck.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
               finish();
 
            }
        });
        
        
        spd25.setOnClickListener(new OnClickListener() {
        	public void onClick(View arg0) {
        		MainActivity.slm.submit(MainActivity.myName, MainActivity.myEmail, 25, 0);
        	}
        });
        
        
        
        
        
    }//end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sign_submitter, menu);
        return true;
    }
}

package com.example.androidsensors;


import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class AndroidSensors extends Activity {
	ArrayList<String> options = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_sensors);
    }
    
    public void start(View view){
    	int first = R.id.movementCB;
    	CheckBox v;
    	while((v=(CheckBox)findViewById(first)) != null){   		
    		if(v.isChecked())
    			options.add(v.getText()+"");
    		first++;
    	}    	 
    }
    
    public void reset(View view){
    	int first = R.id.movementCB;
    	CheckBox v;
    	while((v=(CheckBox)findViewById(first)) != null){   		
    		v.setChecked(false);
    		first++;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_android_sensors, menu);
        return true;
    }
        
}

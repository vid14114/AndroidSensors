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
    
    public void inputStart(View view){
    	int first = R.id.movementCB;
    	CheckBox v;
    	while((v=(CheckBox)findViewById(first)) != null){
    		if(v.isChecked())
    			options.add(v.getText()+"");
    		first++;
    	}    	 
    	setContentView(R.layout.output_methods);
    }
    
    public void inputReset(View view){
    	int first = R.id.movementCB;
    	CheckBox v;
    	try
		{
			while((v=(CheckBox)findViewById(first))!=null)
			{
				v.setChecked(false);
				first++;
			}
		}
		catch(ClassCastException c)
		{
			
		}
    }
    
    public void outputReset(View view)
	{
		int first=R.id.xmlCB;
		CheckBox v;
		try
		{
			while((v=(CheckBox)findViewById(first))!=null)
			{
				v.setChecked(false);
				first++;
			}
		
		}
		catch(ClassCastException c)
		{
			
		}
	}
    
    public void outputNext(View view)
    {
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_android_sensors, menu);
        return true;
    }
        
}

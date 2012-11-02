package com.example.androidsensors;
import java.util.ArrayList;
import java.util.List;

import com.example.androidsensors.R;

import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class AndroidSensors extends Activity{
	ArrayList<String> inputOptions = new ArrayList<String>();
	ArrayList<String> outputOptions = new ArrayList<String>();
	SensorManager sensorManager;
	SensorListener ps;
	LocationManager locMan;
	AndroidLocationListener locListener = new AndroidLocationListener();
	public static final int REQUEST_CODE = 3003;
	protected ArrayList<String> speechResults = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_sensors);
    }
    
    public void inputStart(View view){
    	inputOptions.clear();
    	int first = R.id.movementCB;
    	CheckBox v;
    	while((v=(CheckBox)findViewById(first)) != null){
    		if(v.isChecked())
    			inputOptions.add(v.getText()+"");
    		first++;
    	}   
    	if(inputOptions.size()==0)
    		Toast.makeText(getApplicationContext(), "Select at least one sensor", Toast.LENGTH_SHORT).show();
    	else
    		setContentView(R.layout.output_methods);
    }
    
    public void inputReset(View view){
    	int first = R.id.movementCB;
    	CheckBox v;
    	try{
			while((v=(CheckBox)findViewById(first))!=null){
				v.setChecked(false);
				first++;
			}
		}
		catch(ClassCastException c){}
    }
    
    public void outputReset(View view){
		int first=R.id.xmlCB;
		CheckBox v;
		try{
			while((v=(CheckBox)findViewById(first))!=null){
				v.setChecked(false);
				first++;
			}
		}
		catch(ClassCastException c){}
	}
    
    public void outputNext(View view){
    	outputOptions.clear();
    	int first = R.id.xmlCB;
    	CheckBox v;
    	try{
    		while((v=(CheckBox)findViewById(first)) != null){
	    		if(v.isChecked())
	    			outputOptions.add(v.getText()+"");
	    		first++;
	    	}
    	}catch(ClassCastException c){}
    	if(outputOptions.size()==0)
    		Toast.makeText(getApplicationContext(), "Select at least one output method", Toast.LENGTH_SHORT).show();
    	else{
    		if(inputOptions.contains("GPS"))
    			startListen();
    		startSensors();
    		Handler h = new Handler();
    		h.postDelayed(new Coordinator(inputOptions, outputOptions, this, h), 10000);
    	}
    }
    
    /**
     * Start listening for the location of the user
     */
    public void startListen() {
    	locMan = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    	locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
	}

	/**
     * Prompts the user to speak
     * @param withRecognizer A boolean indicating whether to enable speech recognizer or not
     */
    public boolean speak(boolean withRecognizer){
    	if(withRecognizer){
    		PackageManager pm = getPackageManager();
    		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
    		if(activities.size() == 0)//No recognizer
    			return false;
    		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak");
            startActivityForResult(intent, REQUEST_CODE );
            return true;
    	}
    	else{
    		MediaRecorder mr = new MediaRecorder();
    		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
    		return false;
    	}	
    }
    
    /**
     * Is called after the speech has been through the recognizer
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            speechResults = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS); //Write the result into our array
        if(speechResults.isEmpty())
        	speak(true);
    }

    /**
     * The method is used to display error messages as a Toast message for the user
     * @param error
     */
    public void displayError(String error){
    	Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_android_sensors, menu);
        return true;
    }
    
    public void onPause(){
    	sensorManager.unregisterListener(ps);
    	locMan.removeUpdates(locListener);
    }
    
    /**
     * Registers all the sensors the user needs
     */
	public void startSensors(){
		Sensor sensor;
		ps = new SensorListener();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		try{
			for(String s : inputOptions)
			{
				if(s.equals("Movement") || s.equals("Direction"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
				if(s.equals("Accelerometer"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
				if(s.equals("Magnet"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
				if(s.equals("Air Pressure"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
				if(s.equals("Temperature"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
				if(s.equals("Light"))
				{
					sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
					sensorManager.registerListener(ps, sensor, SensorManager.SENSOR_DELAY_FASTEST);
				}
			}
		}catch(NullPointerException e){ //If the sensor isn't available on the phone, sensor is null
			Toast.makeText(getApplicationContext(), "Sorry, but one of the sensor types you chose is not available on your mobile phone", Toast.LENGTH_SHORT).show();
		}
	}
}

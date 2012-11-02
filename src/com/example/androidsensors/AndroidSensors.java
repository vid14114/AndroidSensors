package com.example.androidsensors;
import java.util.ArrayList;
import java.util.List;
import com.example.androidsensors.R;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidSensors extends Activity{
	ArrayList<String> inputOptions = new ArrayList<String>();
	ArrayList<String> outputOptions = new ArrayList<String>();
	SensorManager sensorManager;
	SensorListener ps;
	LocationManager locMan;
	AndroidLocationListener locListener = new AndroidLocationListener();
	String phonenumber;
	public static final int REQUEST_CODE = 3003;
	protected ArrayList<String> speechResults = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_sensors);
    }
    
    /**
     * When the user clicks on ok, on the input methods menu
     * @param view
     */
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
    		displayMessage("Select at least one sensor");
    	else
    		setContentView(R.layout.output_methods);
    }
    
    /**
     * We uncheck all input checkboxes, when the user clicks on reset
     * @param view
     */
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
    
    /**
     * When the user clicks on reset on the output method view
     * We uncheck all checkboxes 
     * @param view
     */
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
    
    /**
     * called when the user clicks on next in the view
     * @param view
     */
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
    		displayMessage("Select at least one output method");
    	else{
    		if(inputOptions.contains("GPS"))
    			startListen();
    		if(outputOptions.contains("Phone Call") || outputOptions.contains("SMS")){ //If the user selects Phone Call or SMS as an output method -> i do the following
    			/*
    			 * I call the setPositiveButton, setNegativeButton and create method to do all the necessary
    			 * things before i continue 
    			 * A dialog pops up asking the user to input their phone number
    			 */
    			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    			LayoutInflater inflater = getLayoutInflater();
    			builder.setView(inflater.inflate(R.layout.number_dialog, null))
    				.setPositiveButton(R.id.dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							TextView tv = (TextView)findViewById(R.id.phoneNumber);
							phonenumber = tv.getText()+"";
						}
					})
					.setNegativeButton(R.id.dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							phonenumber = null;
							outputOptions.remove("Phone Call");
						}
					})
					.create();
    		}
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
     * The method is called by the {@link Coordinator} class to place a call to a number
     */
    public boolean call(){
    	try{
	    	Intent t = new Intent(Intent.ACTION_CALL);
	    	t.setData(Uri.parse("tel:"+phonenumber));
	    	startActivity(t);
	    	return true;
    	}catch(ActivityNotFoundException e){displayMessage("Couldn't place call"); return false;}
    }
    
    public void sendSMS(String message){
    	PendingIntent sent = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
    	registerReceiver(new BroadcastReceiver() {			
			@Override
			public void onReceive(Context context, Intent intent) {
				if(getResultCode() == Activity.RESULT_OK)
					displayMessage("SMS successfully sent");
				else
					displayMessage("Error sending SMS");
			}
		}, new IntentFilter("SENT"));
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage(phonenumber, null, message, sent, null);
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
     * The method is used to display messages as Toast to the user
     * @param message The message to be displayed to the user
     */
    public void displayMessage(String message){
    	Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

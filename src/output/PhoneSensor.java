/**
 * 
 */
package output;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.service.textservice.SpellCheckerService.Session;

/**
 * @author abideen
 * Many types of output, mainly twp types: real-time like view, and non real time
 */
public class PhoneSensor extends Activity implements SensorEventListener{		
	private SensorManager sensorManager;
	private Sensor sensor;
	public static float[] movement;
	public static float[] accelerometer;
	public static float[] magnet;
	public static float[] direction;
	public static float[] temperature;
	public static float[] light;
	
	
	public PhoneSensor(ArrayList<String> options){
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		//if 
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);			
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
	}
		
	/*
	 * (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 * public static float[] movement;
	public static float[] accelerometer;
	public static float[] magnet;
	public static float[] direction;
	public static float[] temperature;
	public static float[] light;
	 */
	public void onSensorChanged(SensorEvent event) { 
		if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION);
			movement = event.values;
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER);
			accelerometer = event.values;
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD);
			magnet = event.values;
		if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION);
			direction = event.values;
		if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE);
			temperature = event.values;
		if(event.sensor.getType() == Sensor.TYPE_LIGHT)
			light = event.values;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}

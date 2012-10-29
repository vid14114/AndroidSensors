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
 * Get the values of the user-selected sensors
 */
public class PhoneSensor extends Activity implements SensorEventListener{		
	private SensorManager sensorManager;
	private Sensor sensor;
	public static float[] movementDirection;
	public static float[] accelerometer;
	public static float[] magnet;
	public static float[] direction;
	public static float[] temperature;
	public static float[] light;
	
	
	public PhoneSensor(ArrayList<String> options){
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		for(String s : options)
		{
			if(s.equals("Movement") || s.equals("Direction"))
			{
				sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(s.equals("Acceleration"))
			{
				sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(s.equals("Magnet"))
			{
				sensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(s.equals("Air Pressure"))
			{
				sensor=sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(s.equals("Temperature"))
			{
				sensor=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
			if(s.equals("Light"))
			{
				sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
				sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
			}
		}		
	}
	
	public void onSensorChanged(SensorEvent event) { 
		if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION);
			movementDirection = event.values;
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER);
			accelerometer = event.values;
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD);
			magnet = event.values;
		if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE);
			temperature = event.values;
		if(event.sensor.getType() == Sensor.TYPE_LIGHT)
			light = event.values;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
}

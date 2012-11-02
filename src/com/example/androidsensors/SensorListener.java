/**
 * 
 */
package com.example.androidsensors;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
/**
 * Get the values of the user-selected sensors
 */
public class SensorListener implements SensorEventListener{	
	public static float[] movementDirection;
	public static float[] accelerometer;
	public static float[] magnet;
	public static float pressure;
	public static float temperature;
	public static float light;
	
	//register only sensors the user needs

	
	public void onSensorChanged(SensorEvent event) { 
		if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION);
			movementDirection = event.values;
		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER);
			accelerometer = event.values;
		if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD);
			magnet = event.values;
		if(event.sensor.getType() == Sensor.TYPE_PRESSURE)
			pressure = event.values[0];
		if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE);
			temperature = event.values[0];
		if(event.sensor.getType() == Sensor.TYPE_LIGHT)
			light = event.values[0];
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
}

package com.example.androidsensors;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class AndroidLocationListener implements LocationListener {
	public static Location locationInfo;

	public void onLocationChanged(Location location) {
		 locationInfo = location;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onProviderEnabled(String provider) {			
	}

	public void onProviderDisabled(String provider) {
	}

}

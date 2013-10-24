package com.example.mylocapp;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	private LocationManager locationManager;
	private String provider;
	private boolean isGPSEnabled, isNetworkEnabled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    
	    // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        provider = locationManager.getBestProvider(criteria, true);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) {
	    	Log.v("DDLOG", "Provider " + provider + " has been selected.");
	    	onLocationChanged(location);
	    } else {
	    	Toast.makeText(this, "Problem", Toast.LENGTH_SHORT).show();
	    	//latituteField.setText("Location not available");
	    	//longitudeField.setText("Location not available");
	    }
	}
	
	  /* Request updates at startup */
	  @Override
	  protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(provider, 400, 1, this);
	  }
	
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	    int lat = (int) (location.getLatitude());
	    int lng = (int) (location.getLongitude());
    	Toast.makeText(this, "Found the location : " + lat + " " + lng, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	    Toast.makeText(this, "Disabled provider " + provider,
	            Toast.LENGTH_SHORT).show();	
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	    Toast.makeText(this, "Enabled provider " + provider,
	            Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}

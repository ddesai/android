package com.example.calllogstest;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {
	RetrieveAppsInfo rai = null;
	RetrieveContacts rc = null; 
	RetrieveCallLogs rcl = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rai = new RetrieveAppsInfo(this);
		rc = new RetrieveContacts(getContentResolver());
		rcl = new RetrieveCallLogs(getContentResolver());
	}

	public void onClickPermissionsList(View v) {
		rai.printAppsPerms();
	}

	public void onClickPhoneContacts(View v) {
		rc.getContacts();
	}

	public void onClickCallLogs(View v) {
		rcl.getCallLogs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

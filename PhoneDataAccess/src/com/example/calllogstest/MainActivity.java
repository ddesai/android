package com.example.calllogstest;


import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		readContacts();
	}

	public void readContacts() {
		StringBuffer sb = new StringBuffer();
		sb.append("My Contacts List");
		ContentResolver cr = getContentResolver();
		
		//RetrieveContacts rc = new RetrieveContacts(cr); 
		//rc.getContacts();
		//RetrieveCallLogs rcl = new RetrieveCallLogs(cr);
		//rcl.getCallLogs();
		
		RetrieveAppsInfo rai = new RetrieveAppsInfo(this);
		//rai.printAppsInfo();
		rai.printAppsPerms();
		System.out.println(sb);
		Log.v("DD",sb.toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

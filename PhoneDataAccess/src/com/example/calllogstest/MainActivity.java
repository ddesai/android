package com.example.calllogstest;


import android.app.Activity;
import android.os.AsyncTask;
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
		PrintAppsPermsTask task = new PrintAppsPermsTask();
		task.execute();
	}

	public void onClickPhoneContacts(View v) {
		getContactsHelperThread();
	}

	public void onClickCallLogs(View v) {
		PrintCallLogsTask task = new PrintCallLogsTask();
		task.execute();
	}

	// use of AsyncTask to create a parallel thread to obtain the apps permissions info
	private class PrintAppsPermsTask extends AsyncTask<Void, Void, Void> {
		   @Override
		    protected Void doInBackground(Void... v) {
			   rai.printAppsPerms();
			   return null;
		   }
	}

	// use of AsyncTask to create a parallel thread to obtain the apps call logs info
	private class PrintCallLogsTask extends AsyncTask<Void, Void, Void> {
		   @Override
		    protected Void doInBackground(Void... v) {
			   rcl.getCallLogs();
			   return null;
		   }
	}

	// use of Runnable to create a parallel thread 
	private void getContactsHelperThread() {
		// do something long
	    Runnable runnable = new Runnable() {
	      @Override
	      public void run() {
	    	  rc.getContacts();
	      }
	    };
	    new Thread(runnable).start();
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

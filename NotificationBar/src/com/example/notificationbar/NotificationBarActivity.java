package com.example.notificationbar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class NotificationBarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_bar);
		initNotification();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification_bar, menu);
		return true;
	}

	private void initNotification() {
		// prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(this, NotificationBarActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

	    // Build notification
	    // Actions are just fake
	    Notification noti = new Notification.Builder(this)
	        .setContentTitle("New mail from " + "test@gmail.com")
	        .setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
	        .setContentIntent(pIntent)
	        .addAction(R.drawable.ic_launcher, "Call", pIntent)
	        .addAction(R.drawable.ic_launcher, "More", pIntent)
	        .addAction(R.drawable.ic_launcher, "And more", pIntent).build();
	    
	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    // hide the notification after its selected
	    noti.flags |= Notification.FLAG_AUTO_CANCEL;
	    notificationManager.notify(0, noti);
	}
}

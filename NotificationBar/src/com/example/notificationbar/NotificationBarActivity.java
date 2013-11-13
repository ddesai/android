package com.example.notificationbar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

		Bitmap icon_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher); 
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setLargeIcon(icon_bitmap);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle("Darshan B Desai");
		mBuilder.setContentText("ddesai11@gmail.com");
		mBuilder.setContentIntent(pIntent);		
		// This (different actions) will not work until we create BroadcastReceiver
		// mBuilder.addAction(R.drawable.ic_launcher, "Call", pIntent);
		Notification noti = mBuilder.build();
	    noti.flags |= Notification.FLAG_NO_CLEAR;

	    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    notificationManager.notify(0, noti);
	}
}

package com.example.mybasiccalculator;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyCalc extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile"; 
	
    @Override
    // get Called #1 when the activity is created first time
    // #1 (a)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calc);
    	Log.v("DD-DEBUG", "#1(a) : onCreate()");
    }

    // Called if the app is restarted. Doesnt get called when created 
    // #1 (b) 
    protected void onRestart() {
    	super.onRestart();
    	Log.v("DD-DEBUG", "#1(b) : onRestart()");
    }
    
    // #2
    // called after onCreate() or onRestart()
    protected void onStart() {
    	super.onStart();
    	Log.v("DD-DEBUG", "#2  : onStart()");
    }

    @Override
    // #3 
    // should get called after onStart()
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	Log.v("DD-DEBUG", "#3  : onRestoreInstanceState()");
    	restoreMyData();
    }

    @Override
    // #3 
    // called after onRestoreInstanceState()
    protected void onResume() {
    	super.onResume();
    	Log.v("DD-DEBUG", "#3  : onResume()");
    	restoreMyData();
    }

    @Override
    // #1 to be called when activity goes to background or killed
    // #
    protected void onSaveInstanceState (Bundle outState) {
    	super.onSaveInstanceState(outState);
    	Log.v("DD-DEBUG", "#5 (#1): onSaveInstanceState()");
    	preserveMyData();
    }

    // called after onSaveInstanceState()
    // #1 method to be called when activity goes to the background or killed
    // #5
    protected void onPause() {
    	super.onPause();
    	Log.v("DD-DEBUG", "#5 (#1): onPause()");
    }

    // called after onPause()
    // #2 method to be called when activity goes to the background or killed
    // #6
    protected void onStop() {
    	super.onStop();
    	Log.v("DD-DEBUG", "#6 (#2): onStop()");
    }

    // Gets called only when system is low on resources and kills the app
    // Or if the app is destroyed by explicitely calling finish()
    // #7 (#3)
    protected void onDestory() {
    	super.onDestroy();
    	Log.v("DD-DEBUG", "#7 (#3): onDestroy()");
    }
  

    
    public void preserveMyData() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = settings.edit();
    	    	
    	String num1String = ((EditText) findViewById(R.id.editNum1)).getText().toString();
    	String num2String = ((EditText) findViewById(R.id.editNum2)).getText().toString();
    	String op = ((TextView) findViewById(R.id.textOperation)).getText().toString();
    	String resultString = ((TextView) findViewById(R.id.textResult)).getText().toString();

    	editor.putString("num1", num1String); 
    	editor.putString("num2", num2String); 
    	editor.putString("op", op); 
    	editor.putString("result", resultString); 
    	editor.commit(); // Commit the edits!
    	Log.v("DD-Debug", "Preserving the Data");
    }

    public void restoreMyData() {
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
    	String num1String = settings.getString("num1", "");
    	String num2String = settings.getString("num2", "");
    	String op = settings.getString("op", "");
    	String resultString = settings.getString("result", "");
    	
    	EditText editNum1 = (EditText) findViewById(R.id.editNum1);
    	EditText editNum2 = (EditText) findViewById(R.id.editNum2);
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	TextView textResult = (TextView) findViewById(R.id.textResult);
    	
    	textOp.setText(op);
    	editNum1.setText(num1String);
    	editNum2.setText(num2String);
    	textResult.setText(resultString);

    	Log.v("DD-Debug", "Restoring the Data");
    	Log.v("restored the data:", num1String + " " + op + " " + num2String + " = " + resultString);
    }
    	
    // Creates the Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_calc, menu);
        return true;
    }
    
    //Option Menu selection - what to do 
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
    	switch (item.getItemId()) {
    		case R.id.exit:
    			exitCheck();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }

    //Opens up the dialog with Yes/No question
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        	switch (which){
            case DialogInterface.BUTTON_POSITIVE:
            	finish();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
            	break;
            }
        }
    };

    //Asks question if user wants to exit
    public void exitCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure, You want to exit?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show();
    }
    
    public void plusClicked(View v) {
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	textOp.setText("+");
    }

    public void minusClicked(View v) {
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	textOp.setText("-");
    }

    public void mulClicked(View v) {
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	textOp.setText("*");
    }

    public void divClicked(View v) {
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	textOp.setText("/");
    }

    public void clearClicked(View v) {
    	EditText editNum1 = (EditText) findViewById(R.id.editNum1);
    	EditText editNum2 = (EditText) findViewById(R.id.editNum2);
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	TextView textResult = (TextView) findViewById(R.id.textResult);
    	textOp.setText("Op");
    	editNum1.setText("");
    	editNum2.setText("");
    	textResult.setText("");
    }
    
    //Creates the notification in the status bar
    public void notifyStatusBar(String result) {
    	// Get the reference to the NotificationManager
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

    	// Instantiate the notification
    	int icon = R.drawable.ic_launcher;
    	//CharSequence tickerText = "Hello";
    	CharSequence tickerText = result.subSequence(0, result.length()-1);
    	long when = System.currentTimeMillis();
    	Notification notification = new Notification(icon, tickerText, when);

    	// Define notification message and pending intent
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "My Calculator";
    	CharSequence contentText = result;
    	Intent notificationIntent = new Intent(this, MyCalc.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	
    	// Notify the user
    	//private static final int HELLO_ID = 1; // unique id to modify/cancel notification later on
    	mNotificationManager.notify(1, notification);
    }

    
    public void ansClicked(View v) {
    	EditText editNum1 = (EditText) findViewById(R.id.editNum1);
    	EditText editNum2 = (EditText) findViewById(R.id.editNum2);
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	TextView textResult = (TextView) findViewById(R.id.textResult);
    	double ans=0;
    	
    	String num1String = editNum1.getText().toString();
    	String num2String = editNum2.getText().toString();
    	String op = textOp.getText().toString();
    	String operationString = num1String + op + num2String;
    	
    	if (num1String.matches("")) {
    	    Toast.makeText(this, "You did not enter Number1", Toast.LENGTH_LONG).show();
    	    return;
    	}

    	if (num2String.matches("")) {
    	    Toast.makeText(this, "You did not enter Number2", Toast.LENGTH_LONG).show();
    	    return;
    	}

    	if (op.equals("/") && num2String.matches("0")) {
    	    Toast.makeText(this, "You will have Divide by ZERO Error; Change Number2", Toast.LENGTH_LONG).show();
    	    return;
    	}

    	double num1 = Double.valueOf(num1String);
    	double num2 = Double.valueOf(num2String);
    	
    	if(op.equals("+")) {
        	ans = num1 + num2;
    	} else if(op.equals("-")) {
        	ans = num1 - num2;
    	} else if(op.equals("*")) {
        	ans = num1 * num2;
    	} else if(op.equals("/")) {
        	ans = num1 / num2;
    	}    	
    	String ansString = String.valueOf((double) ans);
    	textResult.setText(ansString);
    	
    	ansString = operationString + " = " + ansString;
	    Toast.makeText(this, "Result: "+ansString,
                Toast.LENGTH_LONG).show();
        notifyStatusBar("Result: " + ansString);
        
        /* Starts new activity to show the result */
        Intent i = new Intent(this, CalcResult.class); 
	    i.putExtra("result", ansString);
        startActivity (i);   
    }
}

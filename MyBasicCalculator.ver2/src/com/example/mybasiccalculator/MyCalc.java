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
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyCalc extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calc);
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
        //Intent i = new Intent(this, CalcResult.class); 
	    //i.putExtra("result", ansString);
        //startActivity (i);   
    }
}

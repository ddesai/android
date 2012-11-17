package com.example.mybasiccalculator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CalcResult extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        
        Intent a = getIntent();
        TextView resultText = (TextView) findViewById(R.id.textResult);
        String resultString = "ERROR";
    	if (a.hasExtra("result")) {
    		 resultString = a.getExtras().getString("result");
        }   	
        resultText.setText(resultString);
    }

    public void gobackClicked(View v) {
	    Toast.makeText(this, "goback Clicked", Toast.LENGTH_LONG).show();
	    Intent i = new Intent(this, MyCalc.class); 
	    startActivity (i);
    }
}

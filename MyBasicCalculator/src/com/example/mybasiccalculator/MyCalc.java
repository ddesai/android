package com.example.mybasiccalculator;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_calc, menu);
        return true;
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
    
    public void ansClicked(View v) {
    	EditText editNum1 = (EditText) findViewById(R.id.editNum1);
    	EditText editNum2 = (EditText) findViewById(R.id.editNum2);
    	TextView textOp = (TextView) findViewById(R.id.textOperation);
    	TextView textResult = (TextView) findViewById(R.id.textResult);
    	double ans=0;
    	
    	String num1String = editNum1.getText().toString();
    	String num2String = editNum2.getText().toString();
    	String op = textOp.getText().toString();
    	
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
    }


}

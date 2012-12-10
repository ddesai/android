package com.examples.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class ToDoListSyncActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_sync);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_to_do_list_sync, menu);
        return true;
    }

    public void backClicked(View v)
    {
	    Toast.makeText(this, "Going back", Toast.LENGTH_LONG).show();
	    Intent i = new Intent(this, ToDoList.class); 
	    startActivity (i);
    }
}

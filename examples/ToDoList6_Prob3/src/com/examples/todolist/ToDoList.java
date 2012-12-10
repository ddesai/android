package com.examples.todolist;

import com.examples.todolist.ToDoListService.LocalBinder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


public class ToDoList extends Activity {
	
  static final private int ADD_NEW_TODO = Menu.FIRST;
  static final private int REMOVE_TODO = Menu.FIRST + 1;
  static final private String DTAG = "DD-DEBUG";
  
  private static final String TEXT_ENTRY_KEY = "TEXT_ENTRY_KEY";
  private static final String ADDING_ITEM_KEY = "ADDING_ITEM_KEY";
  private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";
  
  private boolean addingNew = false;
  private int removeItemIndex = -1;
  private ArrayList<ToDoItem> todoItems; 
  private ListView myListView;
  private EditText myEditText;
  
  private ToDoItemAdapter aa; 
  
  ToDoListService todoService;
  Context currentContext;
  boolean todoServiceBound = false;

  
  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
	Log.d(DTAG, "inside onCreate...");

    myListView = (ListView)findViewById(R.id.myListView);
    myEditText = (EditText)findViewById(R.id.myEditText);

    todoItems = new ArrayList<ToDoItem>();
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, todoItems);
    myListView.setAdapter(aa);

    myEditText.setOnKeyListener(new OnKeyListener() {
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            ToDoItem newItem = new ToDoItem(myEditText.getText().toString(), 0);
            
            if (todoServiceBound) 
                todoService.insertRecord(newItem);
            
            updateArray();  
            myEditText.setText("");
            aa.notifyDataSetChanged();
            cancelAdd();
            return true; 
          }
        return false;
      }
    });

    registerForContextMenu(myListView);
    restoreUIState();
    currentContext = this;
  }

  @Override
  public void onStart() {
	  super.onStart();
	Log.d(DTAG, "inside onStart...");
	  if(!todoServiceBound) {
	    //Binding to the ToDoList Service
		Intent intent = new Intent(this, ToDoListService.class);
		Log.d(DTAG, "going to try to bind the service now...");
	    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	    
	    /*
	    if (todoServiceBound) 
	        todoService.init(this);
	    populateTodoList();
	    */
	  }	  
  }
  
  private void populateTodoList() {
    // Update the array.
    updateArray();
  }

  public void syncClicked(View v) 
  {
      Intent i = new Intent(this, ToDoListSyncActivity.class); 
	    //i.putExtra("result", ansString);
      startActivity (i);   
  }

  private void updateArray() {
	  if (!todoServiceBound)
		  Log.d(DTAG,"ToDoService not bound yet; Crashing!");

	  todoItems.clear();
	  
	  // Update the ToDoList ArrayList with the latest data
	  todoService.refresh(todoItems);
	    
	  aa.notifyDataSetChanged();
	}

/*
  private void updateArray() {
	  toDoListCursor.requery();

	  todoItems.clear();
	    
	  if (toDoListCursor.moveToFirst())
	    do { 
	      String task = toDoListCursor.getString(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_TASK));
	      long created = toDoListCursor.getLong(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_CREATION_DATE));
	      int taskid = toDoListCursor.getInt(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_ID));
	      ToDoItem newItem = new ToDoItem(task, new Date(created), taskid);
	      todoItems.add(0, newItem);
	    } while(toDoListCursor.moveToNext());
	  
	  aa.notifyDataSetChanged();
	}
*/
  
  private void restoreUIState() {
    // Get the activity preferences object.
    SharedPreferences settings = getPreferences(0);

    // Read the UI state values, specifying default values.
    String text = settings.getString(TEXT_ENTRY_KEY, "");
    Boolean adding = settings.getBoolean(ADDING_ITEM_KEY, false);

    // Restore the UI to the previous state.
    if (adding) {
      addNewItem();
      myEditText.setText(text);
    }
  }
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt(SELECTED_INDEX_KEY, myListView.getSelectedItemPosition());

    super.onSaveInstanceState(outState);
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    int pos = -1;

    if (savedInstanceState != null)
      if (savedInstanceState.containsKey(SELECTED_INDEX_KEY))
        pos = savedInstanceState.getInt(SELECTED_INDEX_KEY, -1);

    myListView.setSelection(pos);
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    
    // Get the activity preferences object.
    SharedPreferences uiState = getPreferences(0);
    // Get the preferences editor.
    SharedPreferences.Editor editor = uiState.edit();

    // Add the UI state preference values.
    editor.putString(TEXT_ENTRY_KEY, myEditText.getText().toString());
    editor.putBoolean(ADDING_ITEM_KEY, addingNew);
    // Commit the preferences.
    editor.commit();
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    // Create and add new menu items.
    MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
                                R.string.add_new);
    MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE,
                                R.string.remove);

    // Assign icons
    itemAdd.setIcon(R.drawable.add_new_item);
    itemRem.setIcon(R.drawable.remove_item);

    // Allocate shortcuts to each of them.
    itemAdd.setShortcut('0', 'a');
    itemRem.setShortcut('1', 'r');

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    int idx = myListView.getSelectedItemPosition();

    String removeTitle = getString(addingNew ? 
                                   R.string.cancel : R.string.remove);

    MenuItem removeItem = menu.findItem(REMOVE_TODO);
    removeItem.setTitle(removeTitle);
    removeItem.setVisible(addingNew || idx > -1);

    return true;
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, 
                                  View v, 
                                  ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    menu.setHeaderTitle("Selected To Do Item");
    menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    int index = myListView.getSelectedItemPosition();

    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        if (addingNew) {
          cancelAdd();
        } 
        else {
          removeItem(index);
        }
        return true;
      }
      case (ADD_NEW_TODO): { 
        addNewItem();
        return true; 
      }
    }

    return false;
  }
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {  
    super.onContextItemSelected(item);
    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;

        	removeItem(index);
        return true;
      }
    }
    return false;
  }
  
  @Override
  public void onDestroy() {
    super.onDestroy();
      
    // Close the database
    //toDoDBAdapter.close();
 
    // Unbinds from the service
    // it will also close the db
    if (todoServiceBound) {
        unbindService(mConnection);
        todoServiceBound = false;
    }
  }
  
  private void cancelAdd() {
    addingNew = false;
    myEditText.setVisibility(View.GONE);
  }

  private void addNewItem() {
    addingNew = true;
    myEditText.setVisibility(View.VISIBLE);
    myEditText.requestFocus(); 
  }

  private void removeItem(int _index) {
      // Code added to bring up the Dialog box to double check
      // if the user for sure want to remove the item from the ToDo List
      removeItemIndex = _index;
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setMessage("Are you sure, You want to remove?").setPositiveButton("Yes", dialogClickListener)
          .setNegativeButton("No", dialogClickListener).show();
      Log.d(DTAG, "removeItem() called...");
   }

  private void removeItemHelper(int _index)
  {
	     ToDoItem item = todoItems.get(_index);
	     final long selectedId = item.getTaskId();
   	  	 Log.d(DTAG, "removeItemHelper removing the record...");
	     todoService.removeRecord(selectedId);
		 updateArray();   	  
		 Log.d(DTAG, "Item is removed: "+_index);	  
  }

  //Opens up the dialog with Yes/No question
  DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
      	switch (which){
          case DialogInterface.BUTTON_POSITIVE:
          {
        	  Log.d(DTAG, "Removing is approved for item#: "+removeItemIndex);
              removeItemHelper(removeItemIndex);
              break;
          }
          case DialogInterface.BUTTON_NEGATIVE:
          {
        	  Log.d(DTAG, "Removing is NOT approved for item#: "+removeItemIndex);
          	  break;
          }
      }
      }
  };

  /** Defines callbacks for service binding, passed to bindService() */
  private ServiceConnection mConnection = new ServiceConnection() {
      public void onServiceConnected(ComponentName className,
              IBinder service) {
          // We've bound to LocalService, cast the IBinder and get LocalService instance
    	  Log.d(DTAG, "onServiceConnected...");
          LocalBinder binder = (LocalBinder) service;
          todoService = binder.getService();
          todoServiceBound = true;
          
  	    	todoService.init(currentContext);
  	    	populateTodoList();

    	  Log.d(DTAG, "onServiceConnected successfully completed");
      }

      public void onServiceDisconnected(ComponentName arg0) {
          todoServiceBound = false;
      }
  };

}
package com.examples.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ToDoListService extends Service {
	  static final private String DTAG = "DD-DEBUG";

	  ToDoDBAdapter toDoDBAdapter; 
	  Cursor toDoListCursor; 
	  Context currentContext; 
	  int totalRecordsCount = 0;

	
	// Binder given to clients
    private final IBinder mBinder = (IBinder) new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ToDoListService getService() {
      	  	Log.d(DTAG, "inside Service getService...");
            // Return this instance of LocalService so clients can call public methods
            return ToDoListService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
  	  	Log.d(DTAG, "inside Service onBind...");
        return mBinder;
    }
    
    @Override
    public void onDestroy() {
  	  	Log.d(DTAG, "inside Service onDestroy...");
        // Close the database
        toDoDBAdapter.close();
    }

    /** method for clients */
    // Activity should call this first to handshake!
    // and Creates the DBAdapter and get the cursor
    public void init(Context _context) {
  	  	Log.d(DTAG, "inside Service init...");
        toDoDBAdapter = new ToDoDBAdapter(_context);  // DD-Moved
        // Open or create the database
        toDoDBAdapter.open();  // DD-Moved
        // Get all the todo list items from the database.
        toDoListCursor = toDoDBAdapter.getAllToDoItemsCursor();
        ((Activity)_context).startManagingCursor(toDoListCursor);    
        Toast.makeText(this, "Service Initialized and Database connection established", 
        		Toast.LENGTH_SHORT).show();
        currentContext = _context;
  	  	doToast();
    }

    public void refresh(ArrayList<ToDoItem> todoItems) {
  	  	Log.d(DTAG, "inside Service refresh...");
  	  toDoListCursor.requery();
	  if (toDoListCursor.moveToFirst())
		    do { 
		      String task = toDoListCursor.getString(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_TASK));
		      long created = toDoListCursor.getLong(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_CREATION_DATE));
		      int taskid = toDoListCursor.getInt(toDoListCursor.getColumnIndex(ToDoDBAdapter.KEY_ID));
		      ToDoItem newItem = new ToDoItem(task, new Date(created), taskid);
		      todoItems.add(0, newItem);
		    } while(toDoListCursor.moveToNext());
      Toast.makeText(this, "Service refreshed the ToDoList", 
      		Toast.LENGTH_SHORT).show();
      totalRecordsCount = this.totalRecords();
    }
    
    // Total # of records
    public int totalRecords() {
  	  	Log.d(DTAG, "inside Service totalRecords...");
    	return toDoListCursor.getCount();
    }

    // Removes the record
    public void removeRecord(long _index) {
  	  	Log.d(DTAG, "inside Service removeRecord...");
        Toast.makeText(this, "Service removed one record from the DB", 
          		Toast.LENGTH_SHORT).show();
    	toDoDBAdapter.removeTask(_index);
    }
    
    // Inserts the record
    public void insertRecord(ToDoItem newItem) {
  	  	Log.d(DTAG, "inside Service insertRecord...");
        Toast.makeText(this, "Service inserted one record: "+newItem, 
          		Toast.LENGTH_SHORT).show();
    	toDoDBAdapter.insertTask(newItem);
      }

    // Updates the record
    public boolean updateRecord(int _index, String _task) {
  	  	Log.d(DTAG, "inside Service updateRecord...");
        Toast.makeText(this, "Service updated the ToDoList with: "+_task, 
          		Toast.LENGTH_SHORT).show();
    	return toDoDBAdapter.updateTask(_index, _task);
    }

    TimerTask toastTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    int TIMER_STOP_COUNT = 15;
    

    public void doToast(){
    	toastTask = new TimerTask() {
            public void run() {
            	handler.post(new Runnable() {
            		public void run() {
                        if(TIMER_STOP_COUNT-- > 0) {
                        	Toast.makeText(currentContext, "Total pending tasks: "+totalRecordsCount, 
                        			Toast.LENGTH_SHORT).show();
                            Log.d("TOAST TIMER", "Toast Timer Fired: "+TIMER_STOP_COUNT);
                        } else
                        	stopToast();
            		}
            	});
            }};
            t.schedule(toastTask, 200, 20000); 
     }

      public void stopToast(){
    	  if(toastTask!=null){
       			Toast.makeText(currentContext, "Pending Tasks Counting Toast Stopped ", 
       				Toast.LENGTH_LONG).show();
       			Log.d("TOAST TIMER", "Toast Timer Stopped");
       			toastTask.cancel();
    	  }
      }    
}
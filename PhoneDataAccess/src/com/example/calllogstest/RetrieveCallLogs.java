package com.example.calllogstest;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

class RetrieveCallLogs {
	ContentResolver cr = null;

	public RetrieveCallLogs(ContentResolver cr1) {
		cr = cr1;
	}
	
	public void getCallLogs() {
		Uri CALL_LOG_URI = CallLog.Calls.CONTENT_URI;
		String NUMBER = CallLog.Calls.NUMBER;
		String NAME = CallLog.Calls.CACHED_NAME;
		String NUMBER_TYPE = CallLog.Calls.CACHED_NUMBER_TYPE; 
		String CALL_TYPE = CallLog.Calls.TYPE;
		String CALL_DATE = CallLog.Calls.DATE;
		String CALL_DURATION = CallLog.Calls.DURATION;

	    Cursor cur = cr.query(CALL_LOG_URI, null, null, null, null);
	    if(cur != null && cur.getCount() >0) {
		    while(cur.moveToNext()) {
		    	String record = new String();
			    String number = cur.getString(cur.getColumnIndex(NUMBER));
			    String name = cur.getString(cur.getColumnIndex(NAME));
			    String number_type = cur.getString(cur.getColumnIndex(NUMBER_TYPE));
			    int call_type = Integer.parseInt(cur.getString(cur.getColumnIndex(CALL_TYPE)));
			    Date call_date = new Date(Long.valueOf(
			    		cur.getString(cur.getColumnIndex(CALL_DATE))));
			    int call_duration = Integer.parseInt(cur.getString(cur.getColumnIndex(CALL_DURATION)))/60;
			    String call_type_str = null;

			    switch(call_type) {
			    	case CallLog.Calls.OUTGOING_TYPE:
			    		call_type_str = "Outgoing";
			    		break;
			    	case CallLog.Calls.INCOMING_TYPE:
			    		call_type_str = "Incoming";
			    		break;
			    	case CallLog.Calls.MISSED_TYPE:
			    		call_type_str = "Missed";
			    		break;
			    }
			    record = name + " " + number + " (" + number_type + ") ";
			    record = record + call_type_str + " " + call_date + " " + call_duration + " mins";
			    Log.v("DD",record);
		   }
	   }
	   cur.close();
	}
}


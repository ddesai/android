package com.example.calllogstest;

import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;

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

class RetrieveContacts {
	ContentResolver cr = null;

	public RetrieveContacts(ContentResolver cr1) {
		cr = cr1;
	}
	
	public void getContacts() {
		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
	    String ContactsProjection[] = {_ID, DISPLAY_NAME, HAS_PHONE_NUMBER};
	    Cursor cur = cr.query(CONTENT_URI, ContactsProjection, null, null, null);
	    if(cur != null && cur.getCount() >0) {
		    while(cur.moveToNext()) {
		    	String record = new String();
			    String contact_id = cur.getString(cur.getColumnIndex(_ID));
			    String name = cur.getString(cur.getColumnIndex( DISPLAY_NAME));
			    int has_phone = Integer.parseInt(
					   cur.getString(cur.getColumnIndex( HAS_PHONE_NUMBER)));
			    record = record + "Contact_ID: "+contact_id +" Name: "+name;
			    if(has_phone>0) {
				    record += getPhoneNumbers(contact_id);
				    record += getEmails(contact_id);
			    }
			    Log.v("DD",record);
		   }
	   }
	   cur.close();
	}
	
	public String getPhoneNumbers(String contact_id) {
		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER; 
		String NUMBER_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE; 
		String PhoneProjection[] = {Phone_CONTACT_ID, NUMBER, NUMBER_TYPE};
		String phoneCondPref = Phone_CONTACT_ID + " = ? ";
		String phoneContactIDs[] = new String[] {contact_id};
		String phoneNumbers = " Phone Numbers:";		   
		Cursor cur = cr.query(PhoneCONTENT_URI, PhoneProjection, phoneCondPref, phoneContactIDs, null);
			if(cur != null && cur.getCount() >0) {
				while(cur.moveToNext()) {
					String phoneNumber = cur.getString(cur.getColumnIndex(NUMBER));
					String phoneType = cur.getString(cur.getColumnIndex(NUMBER_TYPE));
					phoneNumbers = phoneNumbers + " " + phoneNumber + "(" + phoneType + ")";
				}
			}
			cur.close();
			return phoneNumbers;
		}

	public String getEmails(String contact_id) {
		Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String Email_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
		String EMAIL_TYPE = ContactsContract.CommonDataKinds.Email.TYPE;

		String EmailProjection[] = {Email_CONTACT_ID, EMAIL_DATA, EMAIL_TYPE};
		String emailCondPref = Email_CONTACT_ID + " = ? ";
		String emailContactIDs[] = new String[] {contact_id};
		String emails = " Emails:";		   
		Cursor cur = cr.query(EmailCONTENT_URI, EmailProjection, emailCondPref, emailContactIDs, null);
		if(cur != null && cur.getCount() >0) {
			while(cur.moveToNext()) {
				String email = cur.getString(cur.getColumnIndex(EMAIL_DATA));
				String emailType = cur.getString(cur.getColumnIndex(EMAIL_TYPE));
				emails = emails + " " + email + "(" + emailType + ")";
			}
		}
		   	cur.close();
		   	return emails;
		}
}


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		readContacts();
	}

	public void readContacts() {
		StringBuffer sb = new StringBuffer();
		sb.append("My Contacts List");
		ContentResolver cr = getContentResolver();
		
		//RetrieveContacts rc = new RetrieveContacts(cr); 
		//rc.getContacts();
		RetrieveCallLogs rcl = new RetrieveCallLogs(cr);
		rcl.getCallLogs();
		
		System.out.println(sb);
		Log.v("DD",sb.toString());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

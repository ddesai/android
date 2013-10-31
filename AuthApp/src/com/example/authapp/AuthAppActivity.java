package com.example.authapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

/*
 * 1) Get the Accounts info
 * 2) Get the 
 */
public class AuthAppActivity extends Activity {

	AccountManager am = null;
	AuthSharedPrefs prefs = null;
	
	static final int AUTH_CODE = 0xdead;
	static final int ACT_CODE = 0xbeac;	
	final String AUTH_TOKEN_TYPE = "https://www.googleapis.com/auth/googletalk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth_app);
		
		am = AccountManager.get(this);
		prefs = new AuthSharedPrefs(this);
		
		if(prefs.getUser() != null && prefs.getAuthToken() != null) {
			authSummary();
		} else {
			// get the info about existing acts from the phone
			// selects the account 
			retrieveAccountInfo();
		}
	}

	/* 
	 * Initiate the AccountManager Account Intent
	 * This will start the new Intent, which will return the results
	 * When this intent will have the results ready, it will call
	 * onActivityResult and provides the User Act/Token info
	 */
	void retrieveAccountInfo() {
		String[] actType = new String[] {"com.google"};
		Intent intent=null;
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			intent = AccountManager.newChooseAccountIntent
					(null, null, actType, false, null, null, null, null);
		}
		startActivityForResult(intent, ACT_CODE);
		// When this activity will return, it will call
		// onActivityResult() of this class 
		// this will select the gmail act on the device
		// e.g. abc@gmail.com
	}
	
	
	// gets the new auth token for the given act
	private void requestAuthCode() {
		String username = prefs.getUser();
		Account act = null;
		for (Account account : am.getAccountsByType("com.google")) {
			if (account.name.equals(username)) {
				act = account;
				break;
			}
		}
		am.getAuthToken(act, "oauth2:" + AUTH_TOKEN_TYPE, null, this,
				new AuthTokenReceived(), null);
	}
	
	// invalidates the auth token in the ActMgr
	// updates the local sharedprefs
	private void invalidateAuthCode() {
		AccountManager actMgr = AccountManager.get(this);
		actMgr.invalidateAuthToken("com.google",
				prefs.getAuthToken());
		prefs.setAuthToken(null);
	}
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			// starts from scratch with act info
			// get act info
			// invalidate previous auth code
			// obtain new auth code
			if (requestCode == ACT_CODE) {
				String accountName = data
						.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
				prefs.setUser(accountName);

				//#1 - invalidate Auth Code for this client app
				invalidateAuthCode();
				//#2 - request Auth Code for this client app
				requestAuthCode();
			} else if (requestCode == AUTH_CODE) {
				//#2 - Get Auth Code
				requestAuthCode();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auth_app, menu);
		return true;
	}
	
	private class AuthTokenReceived implements AccountManagerCallback<Bundle> {	 
		@Override
		public void run(AccountManagerFuture<Bundle> results) {
			try {
				Bundle bundle = results.getResult();
 
				Intent userInputIntent = (Intent) bundle.get(AccountManager.KEY_INTENT);
				if (userInputIntent != null) {
					//user input is required to obtain the auth code
					startActivityForResult(userInputIntent, AUTH_CODE);
				} else {
					// auth token is ready! Grab it and use it!
					prefs.setAuthToken(
							bundle.getString(AccountManager.KEY_AUTHTOKEN));
					// do the real meat!
					authSummary();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/* 
	 * This can do the work after the token is available
	 */
	void authSummary() {
		String results = "User: "+prefs.getUser();
		results = results + "\nToken: " + prefs.getAuthToken();
		setResults(results);
		Log.v("DD-Auth", results);
	}
	
	void setResults(String results) {
		TextView resultsView = (TextView) this.findViewById(R.id.result);
		resultsView.setText(results);
	}
}

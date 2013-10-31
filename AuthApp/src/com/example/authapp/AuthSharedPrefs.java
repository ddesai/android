/**
 * 
 */
package com.example.authapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AuthSharedPrefs {

	static final String USER = "user";
	static final String AUTH_TOKEN = "auth_token";
 
	SharedPreferences prefs;
 
	public AuthSharedPrefs(Context context) {
		prefs = context
				.getSharedPreferences("auth", Context.MODE_PRIVATE);
	}

	//gets the user from sharedprefs
	public String getUser() {
		return prefs.getString(USER, null);
	}
 
	//sets the user in sharedprefs
	public void setUser(String user) {
		Editor editor = prefs.edit();
		editor.putString(USER, user);
		editor.commit();
	}
 
	//sets the Token in sharedprefs
	public void setAuthToken(String password) {
		Editor editor = prefs.edit();
		editor.putString(AUTH_TOKEN, password);
		editor.commit();
	}
 
	//gets the Token from sharedprefs
	public String getAuthToken() {
		return prefs.getString(AUTH_TOKEN, null);
	}
}
package com.example.calllogstest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

class RetrieveAppsInfo {
	Activity mAct = null; 
	ArrayList<ApplicationInfo> filteredAppsList = null;
	List<ApplicationInfo> deviceAppsList = null;
	
	RetrieveAppsInfo(Activity act) {
		mAct = act; 
		filteredAppsList = new ArrayList<ApplicationInfo>();  
	}
	
	public void populateAppsList() {
		// Get the Application List
		deviceAppsList = mAct.getPackageManager().
				getInstalledApplications(PackageManager.GET_META_DATA);
		
		Iterator<ApplicationInfo> itr = deviceAppsList.iterator();
		while(itr.hasNext()) {
			ApplicationInfo appInfo = itr.next();
			String appRecord = null;
			appRecord = appInfo.className + " " + appInfo.dataDir + " " + appInfo.permission;
			Log.v("DD", appRecord);
		}	
	}
}


package com.example.calllogstest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

class RetrieveAppsInfo {
	Activity mAct = null; 
	ArrayList<ApplicationInfo> filteredAppsList = null;
	List<ApplicationInfo> deviceAppsList = null;
	PackageManager pm = null;
	Hashtable<String, LinkedList<PackageInfo>> pHash = null;
	
	RetrieveAppsInfo(Activity act) {
		mAct = act; 
		filteredAppsList = new ArrayList<ApplicationInfo>();
		pm = mAct.getPackageManager();
		getAppsList();
		pHash = new Hashtable<String, LinkedList<PackageInfo>>();
	}
	
	public void getAppsList() {
		// Get the Application List
		deviceAppsList = pm.getInstalledApplications(PackageManager.GET_META_DATA);
	}
	
	//prints the basic info about Application
	public void printAppsInfo() {
		Iterator<ApplicationInfo> itr = deviceAppsList.iterator();
		while(itr.hasNext()) {
			ApplicationInfo appInfo = itr.next();
			String appRecord = null;
			appRecord = appInfo.className + " " + appInfo.dataDir + " " + appInfo.permission;
			Log.v("DD", appRecord);
		}	
	}
	
	//prints the permissions given to the applications
	public void printAppsPerms() {
		Iterator<ApplicationInfo> itr = deviceAppsList.iterator();
		while(itr.hasNext()) {
			ApplicationInfo appInfo = itr.next();
			try {
				PackageInfo pkgInfo = pm.getPackageInfo(appInfo.packageName, PackageManager.GET_PERMISSIONS);
				String[] permsList = pkgInfo.requestedPermissions;
				String appRecord = "Pkg: "+pkgInfo.packageName + " Permissions: ";
				if(permsList != null) {
					for(String perm :permsList) {
						//appRecord += perm + " ";
						addToHash(pkgInfo,perm);
					}
				}
				//Log.v("DD", appRecord);
			} catch (NameNotFoundException e) {
				Log.e("DD", "Exception: Couldnt retrieve the Package Info for the app");
				e.printStackTrace();
			}
		}			
	}
	
	public void addToHash(PackageInfo pkg, String perm) {
		List<PackageInfo> value=null;
		if(pHash.containsKey(perm)) {
			value = pHash.get(perm);
			value.add(pkg);
		} else {
			value = new LinkedList<PackageInfo>();
			value.add(pkg);
		}
	}
}


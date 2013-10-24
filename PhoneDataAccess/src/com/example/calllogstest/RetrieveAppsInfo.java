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
	Hashtable<String, List<PackageInfo>> pHash = null;
	
	RetrieveAppsInfo(Activity act) {
		mAct = act; 
		filteredAppsList = new ArrayList<ApplicationInfo>();
		pm = mAct.getPackageManager();
		getAppsList();
		pHash = new Hashtable<String, List<PackageInfo>>();
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
			appRecord = pm.getApplicationLabel(appInfo) + " " + appInfo.dataDir + " " + appInfo.permission;
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
				if(permsList != null) {
					for(String perm :permsList) {
						addToHash(pkgInfo,perm);
					}
				}
			} catch (NameNotFoundException e) {
				Log.e("DD", "Exception: Couldnt retrieve the Package Info for the app");
				e.printStackTrace();
			}
		}	
		summarizePermissions();
	}
	
	//iterates over the hash and prints the list of the packages using a particular permissions
	public void summarizePermissions() {
		Iterator<String> itr = pHash.keySet().iterator();
		Log.v("DD", "SummarizePermissions");
		while(itr.hasNext()) {
			String permission = (String)itr.next();
			List<PackageInfo> value = pHash.get(permission);
			Iterator<PackageInfo> litr = value.iterator();
			Log.v("DD", "Permission: "+permission);
			while(litr.hasNext()) {
				Log.v("DD", "-->"+ (litr.next()).packageName);
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
			pHash.put(perm, value);
		}
	}
}


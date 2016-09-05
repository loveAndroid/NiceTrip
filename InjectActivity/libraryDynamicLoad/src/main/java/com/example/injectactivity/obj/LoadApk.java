package com.example.injectactivity.obj;

import java.util.HashMap;

import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * manager every plugin apk , a plugin file is reflect to a loadapk object
 */
public class LoadApk {
	public String packageName;
	public String sourcePath;
//	public String destPath;
	public String dexPath;
	public AssetManager assetManager;
	public Resources resources;
	public HashMap<String, ActivityInfo> loadActivities = new HashMap<>();
	public ActivityInfo mainActivity;
}

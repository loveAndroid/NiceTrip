package com.example.injectactivity;

import java.util.HashMap;

import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * manager every plugin apk , a plugin file is reflect to a loadapk object
 */
public class LoadApk {
	String packageName;
	String sourcePath;
	String destPath;
	String dexPath;
	AssetManager assetManager;
	Resources resources;
	HashMap<String, ActivityInfo> loadActivities = new HashMap<>();
	ActivityInfo mainActivity;
}

package com.nicetrip.freetrip.util.statusbar;

import android.app.Activity;
import android.graphics.Color;

public class StatusBarUtils {

	public static void set(Activity context){
		SystemBarTintManager tintManager = new SystemBarTintManager(context);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setTintColor(Color.parseColor("#e66463"));
		tintManager.setStatusBarTintColor(Color.parseColor("#e66463"));
	}
	
}

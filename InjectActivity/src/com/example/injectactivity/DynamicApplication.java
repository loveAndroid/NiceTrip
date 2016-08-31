package com.example.injectactivity;

import android.app.Application;
import android.content.Context;

public class DynamicApplication extends Application{

	static Context context = null;
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		context = base;
		Launcher.getInstance().launch(base);
	}
	
}

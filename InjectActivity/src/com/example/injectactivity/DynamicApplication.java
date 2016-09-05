package com.example.injectactivity;

import android.app.Application;
import android.content.Context;

public class DynamicApplication extends Application{

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		Launcher.getInstance().launch(base);
	}
	
}

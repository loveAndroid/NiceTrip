package com.example.injectactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.mainTv).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// load the apk which is in file system
		try {
			Intent intent = new Intent();
			intent.setClassName(getPackageName(), Launcher.getInstance().mPluginApk.mainActivity.name);
			Bundle extras = new Bundle();
			extras.putBoolean("b", false);
			intent.putExtras(extras);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

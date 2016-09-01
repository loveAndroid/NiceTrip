package com.example.injectactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
			intent.setClassName(getPackageName(), Launcher.getInstance().getLoadApks().get(0).mainActivity.name);
			Bundle extras = new Bundle();
			extras.putBoolean("b", false);
			intent.putExtras(extras);
//			startActivity(intent);
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			boolean booleanExtra = data.getBooleanExtra("b", false);
			Toast.makeText(this, "from result b = " + booleanExtra, Toast.LENGTH_SHORT).show();
		}

	}

}

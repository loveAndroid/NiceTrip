package com.example.injectactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AA extends Activity {
	public static String name = "com.example.injectactivity.AA";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		((TextView) findViewById(R.id.mainTv)).setText("i am AA ");
	}
}

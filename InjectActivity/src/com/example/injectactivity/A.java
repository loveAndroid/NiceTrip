package com.example.injectactivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class A extends Activity{
	public static String name = "com.example.injectactivity.A"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView tvMain = (TextView) findViewById(R.id.mainTv);
		tvMain.setText("i am A.class = " + name);
		
	}
}

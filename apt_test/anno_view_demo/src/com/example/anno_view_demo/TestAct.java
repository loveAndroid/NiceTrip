package com.example.anno_view_demo;

import com.azir.anno.InjectUtils;
import com.azir.anno.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

@ViewInject
public class TestAct extends Activity {
	
	@ViewInject(viewId = R.id.mainTv)
	public TextView testTextview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		InjectUtils.injecter(this);
	}
	
}

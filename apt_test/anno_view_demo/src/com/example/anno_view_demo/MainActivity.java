package com.example.anno_view_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.azir.anno.InjectUtils;
import com.azir.anno.ViewInject;

@ViewInject
public class MainActivity extends Activity implements OnClickListener {

	@ViewInject(viewId = R.id.mainTv)
	public TextView tv;
	
	@ViewInject(viewId = R.id.mainImage)
	public ImageView image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		InjectUtils.injecter(this);
		
		System.out.println(tv.getId());
		
		tv.setText("haha ......");
		
		image.setImageResource(android.R.drawable.ic_dialog_alert);
		

		tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent(this, TestAct.class));
	}

}

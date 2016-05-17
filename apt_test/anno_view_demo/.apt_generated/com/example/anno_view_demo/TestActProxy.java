/////////generate by viewinject do not modify it///////// 


package com.example.anno_view_demo;

import com.azir.anno.Injecter;
import android.view.View;
import com.example.anno_view_demo.TestAct;
import com.azir.anno.InjectUtils;

public class TestActProxy<T extends TestAct> implements Injecter<T> { 
	 public TestActProxy() { 

	}

	@Override
	public void inject(T target,View view) { 	
		target.testTextview = InjectUtils.castView(view.findViewById(2131099648));
	}
}
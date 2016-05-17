/////////generate by viewinject do not modify it///////// 


package com.example.anno_view_demo;

import com.azir.anno.Injecter;
import android.view.View;
import com.example.anno_view_demo.MainActivity;
import com.azir.anno.InjectUtils;

public class MainActivityProxy<T extends MainActivity> implements Injecter<T> { 
	 public MainActivityProxy() { 

	}

	@Override
	public void inject(T target,View view) { 	
		target.image = InjectUtils.castView(view.findViewById(2131099649));
		target.tv = InjectUtils.castView(view.findViewById(2131099648));
	}
}
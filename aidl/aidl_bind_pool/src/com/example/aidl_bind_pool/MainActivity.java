package com.example.aidl_bind_pool;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.example.aidl_bind_pool.aidl.IComputer;
import com.example.aidl_bind_pool.aidl.IRun;
import com.example.aidl_bind_pool.aidl.impl.BinderPool;
import com.example.aidl_bind_pool.aidl.impl.ComputerImpl;
import com.example.aidl_bind_pool.aidl.impl.RunImpl;

public class MainActivity extends Activity {

	private BinderPool mBinderPool;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Toast.makeText(getApplicationContext(), "ok", 0).show();
		}
	};
	
	
	public void add(View view){
		

		if(mBinderPool == null){
			return ;
		}
		IComputer computer = ComputerImpl.asInterface(mBinderPool.queryBinder(BinderPool.BINDER_ADD));
		try {
			Toast.makeText(getApplicationContext(), "add = " + computer.add(4, 5), 0).show();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		IRun run = RunImpl.asInterface(mBinderPool.queryBinder(BinderPool.BINDER_RUN));
		
		try {
			Toast.makeText(getApplicationContext(), "run = " + run.getRandom(10), 0).show();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getBinderPool();
	}

	private void getBinderPool() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				
				BinderPool instance = BinderPool.getInstance(MainActivity.this);
				if(instance != null){
					mBinderPool = instance;
					mHandler.obtainMessage().sendToTarget();
				}
				
			}
		}).start();
	}

}

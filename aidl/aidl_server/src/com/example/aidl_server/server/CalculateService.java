package com.example.aidl_server.server;

import com.example.aidl_server.aidl.ICalculate;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

public class CalculateService extends Service {

	private Binder binder = new ICalculate.Stub() {

		@Override
		public int add(int x, int y) throws RemoteException {

			
			
			return x + y;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onbind  add ");
		return binder;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

}

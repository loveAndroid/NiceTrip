package com.example.aidl_client;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;

import com.example.aidl_server.aidl.ICalculate;
import com.example.aidl_server.aidl.IComputerManager;
import com.example.aidl_server.aidl.IOnComputerArrivedListener;
import com.example.aidl_server.entity.ComputerEntity;

public class MainActivity extends Activity {

	private static final int MESSAGE_COMPUTER_ARRIVED = 1;
	ICalculate calculate;


	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			calculate = ICalculate.Stub.asInterface(service);
			System.out.println("connection ..." + calculate);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent temp = new Intent();
		temp.setAction("com.test.aidl.calculate");
		Intent service = new Intent(getExplicitIapIntent(temp));
		bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	public void add(View view) {
		try {
			System.out.println(calculate.add(5, 5));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

	private Intent getExplicitIapIntent(Intent intent) {
		PackageManager pm = this.getPackageManager();
		List<ResolveInfo> resolveInfos = pm.queryIntentServices(intent, 0);

		// Is somebody else trying to intercept our IAP call?
		if (resolveInfos == null || resolveInfos.size() != 1) {
			return null;
		}

		ResolveInfo serviceInfo = resolveInfos.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		Intent iapIntent = new Intent();
		iapIntent.setComponent(component);
		return iapIntent;
	}

}

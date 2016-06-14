package com.example.aidl;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.example.aidl.manual.AidlImpl;
import com.example.aidl.manual.AidlImpl2;
import com.example.aidl.manual.AidlInterface;
import com.example.aidl.manual.AidlListener;
import com.example.aidl.manual.Stub;
import com.example.aidl.utils.Utils;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	protected Stub listener = new Stub() {

		@Override
		public void onChange() throws RemoteException {
			System.out.println("onchange ....... aidl ");
		}
	};
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			System.out.println("main connection...");
			AidlInterface aidl = AidlImpl.asInterface(new AidlImpl2() {
				
				@Override
				public void registerListener(AidlListener listener) {
					
				}
				
				@Override
				public int add(int x, int y) {
					return 100;
				}
			});

			aidl.registerListener(listener);

			System.out.println(aidl.add(5, 4));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		Intent temp = new Intent();
//		temp.setAction("com.nicetrip.freetrip.aidl.manual");
//		Intent service = new Intent(getExplicitIapIntent(temp));
//		bindService(service, conn, Context.BIND_AUTO_CREATE);
//		
//		System.out.println("onCreate");
		
		
		
		AidlImpl2 aidlImpl2 = new AidlImpl2() {
			
			@Override
			public void registerListener(AidlListener listener) {
				
			}
			
			@Override
			public int add(int x, int y) {
				return 999;
			}
		};
		
		
		System.out.println(aidlImpl2.add(5, 5));
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	public void helloword(View view) {
		View viewById = findViewById(R.id.mainRoot);
		Bitmap bgView = Utils.createBitmapFromView(viewById);
		
//		Bitmap rotation = Utils.rotation(bgView, 90);
		
		TextView imgview = (TextView) findViewById(R.id.imageView1);
		imgview.setBackgroundDrawable(new BitmapDrawable(bgView));
		System.out.println(bgView == null);
		
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		System.out.println("onSaveInstanceState");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		System.out.println("onRestoreInstanceState");
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		System.out.println("onConfigurationChanged");
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

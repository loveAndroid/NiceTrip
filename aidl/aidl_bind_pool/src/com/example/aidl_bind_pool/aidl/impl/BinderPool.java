package com.example.aidl_bind_pool.aidl.impl;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.aidl_bind_pool.aidl.IBinderPool;

public class BinderPool {

	public static final int BINDER_NONE = -1;
	public static final int BINDER_ADD = 0;
	public static final int BINDER_RUN = 1;

	private static BinderPool mInstance = new BinderPool();
	private static IBinderPool mBinderPool;
	private static Context mContext;

	private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
		@Override
		public void binderDied() {
			mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
			mBinderPool = null;
			connectionService();
		}
	};

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBinderPool = IBinderPool.Stub.asInterface(service);
			try {
				mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mCountDownLatch.countDown();
		}
	};
	private CountDownLatch mCountDownLatch;

	private BinderPool() {
	}

	private void connectionService() {

		mCountDownLatch = new CountDownLatch(1);
		Intent temp = new Intent();
		temp.setAction("com.nicetrip.binderpool");

		Intent service = getExplicitIapIntent(temp);
		mContext.bindService(service, conn, Context.BIND_AUTO_CREATE);

		try {
			mCountDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static BinderPool getInstance(Context context) {
		mContext = context;
		if (mBinderPool == null) {
			synchronized (BinderPool.class) {
				if(mBinderPool == null){
					return new BinderPool();
				}
			}
		}
		return mInstance;
	}

	private Intent getExplicitIapIntent(Intent intent) {
		PackageManager pm = mContext.getPackageManager();
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

	public IBinder queryBinder(int binderCode) {
		IBinder binder = null;
		if (mBinderPool != null) {
			try {
				binder = mBinderPool.queryBinder(binderCode);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return binder;
	}

	public static class BinderPoolImpl extends IBinderPool.Stub {

		@Override
		public IBinder queryBinder(int binderId) throws RemoteException {
			IBinder binder = null;
			switch (binderId) {
			case BINDER_ADD:
				binder = new ComputerImpl();
				break;
			case BINDER_RUN:
				binder = new RunImpl();
				break;
			default:
				break;
			}
			return binder;
		}

	}
}

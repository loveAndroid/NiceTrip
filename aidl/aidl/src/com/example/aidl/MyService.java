package com.example.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class MyService extends Service {

	RemoteCallbackList<MyListener> mCallbackList = new RemoteCallbackList<>();

	Binder mBinder = new IComputer.Stub() {

		@Override
		public void registerListener(MyListener listener) throws RemoteException {
			mCallbackList.register(listener);
		}

		public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
				throws RemoteException {

			System.out.println("service onTransact");

			return super.onTransact(code, data, reply, flags);
		}

		@Override
		public IBinder asBinder() {
			System.out.println("service asBinder");
			return super.asBinder();
		}

		@Override
		public void attachInterface(IInterface owner, String descriptor) {
			System.out.println("service attachInterface");
			super.attachInterface(owner, descriptor);
		};
	};

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("service onbinder");
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("service onCreate");
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (true) {

					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					int count = mCallbackList.beginBroadcast();

					for (int i = 0; i < count; i++) {
						MyListener listener = mCallbackList.getBroadcastItem(i);
						if (listener != null) {
							try {
								listener.onChange();
							} catch (RemoteException e) {
								e.printStackTrace();
							} finally{
							}
						}
					}
					mCallbackList.finishBroadcast();
				}
			}

		}.start();

	}

}

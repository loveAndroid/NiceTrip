package com.example.aidl.manual;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AidlService extends Service {

	AidlImpl aidlImpl = new AidlImpl() {
		@Override
		public int add(int x, int y) {
			System.out.println("impl add(x,y)");
			int count = mCallbackList.beginBroadcast();

			for (int i = 0; i < count; i++) {
				AidlListener listener = mCallbackList.getBroadcastItem(i);
				if (listener != null) {
					try {
						listener.onChange();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			mCallbackList.finishBroadcast();
			return x + y;
		}

		@Override
		public void registerListener(AidlListener listener) {
			if (listener != null)
				mCallbackList.register(listener);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return aidlImpl;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}
}
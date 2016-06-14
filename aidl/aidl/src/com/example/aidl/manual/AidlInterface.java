package com.example.aidl.manual;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;

public interface AidlInterface extends IInterface {

	static final String DESCRIPTOR = "com.nicetrip.freetrip.aidl.manual";
	
	static final int transaction_add = (IBinder.FIRST_CALL_TRANSACTION + 0);
	static final int transaction_register_listener = (IBinder.FIRST_CALL_TRANSACTION + 1);
	
	RemoteCallbackList<AidlListener> mCallbackList = new RemoteCallbackList<AidlListener>();

	public int add(int x, int y);
	
	public void registerListener(AidlListener listener);
}


package com.example.aidl.manual;


public interface AidlListener extends android.os.IInterface {
	
	public void onChange() throws android.os.RemoteException;
	static final java.lang.String DESCRIPTOR = "com.example.aidl.manual.AidlListener";
	static final int TRANSACTION_onChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
	
	
}

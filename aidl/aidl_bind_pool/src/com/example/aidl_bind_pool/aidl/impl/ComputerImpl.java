package com.example.aidl_bind_pool.aidl.impl;

import android.os.RemoteException;

import com.example.aidl_bind_pool.aidl.IComputer;

public class ComputerImpl extends IComputer.Stub {

	@Override
	public int add(int a, int b) throws RemoteException {
		return a + b;
	}

}

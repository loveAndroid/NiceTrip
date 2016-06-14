package com.example.aidl_bind_pool.aidl.impl;

import java.util.Random;

import android.os.RemoteException;

import com.example.aidl_bind_pool.aidl.IRun;

public class RunImpl extends IRun.Stub {

	@Override
	public int getRandom(int x) throws RemoteException {
		return new Random().nextInt(x);
	}

}

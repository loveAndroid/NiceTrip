package com.example.aidl_bind_pool.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.aidl_bind_pool.aidl.impl.BinderPool;

public class BInderPoolService extends Service {

	Binder binderPool = new BinderPool.BinderPoolImpl();

	@Override
	public IBinder onBind(Intent intent) {
		return binderPool;
	}

}

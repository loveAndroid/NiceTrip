package com.example.aidl_bind_pool.aidl;

import android.os.IBinder;

interface IBinderPool {
	IBinder queryBinder(int binderId);
}

package com.example.small.clipbord;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.os.Build;
import android.util.Log;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by Kongxs on 2016- 09-13.
 */
public class ClipboardManagerHook implements MethodInterceptor {

    private final Object mObj;

    public ClipboardManagerHook(Object object) {
        this.mObj = object;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        if ("getPrimaryClip".equals(method.getName())) {
            return ClipData.newPlainText(null, "you are hooked");
        }
        // 欺骗系统,使之认为剪切版上一直有内容
        if ("hasPrimaryClip".equals(method.getName())) {
            return true;
        }
        return method.invoke(mObj,args);
    }

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }
}

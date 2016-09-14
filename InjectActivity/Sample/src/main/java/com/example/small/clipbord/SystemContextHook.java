package com.example.small.clipbord;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import android.annotation.TargetApi;
import android.os.Build;

/**
 * Created by Kongxs on 2016- 09-13.
 */
public class SystemContextHook implements MethodInterceptor {


    private Object mObj;

    public SystemContextHook(Object obj) {
        this.mObj = obj;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        Class<?> aClass = mObj.getClass();
        Field mClipboardManager = aClass.getField("mClipboardManager");
        
        System.out.println("mClipboardManager = " + mClipboardManager);
        
        mClipboardManager.setAccessible(true);
        Object o = mClipboardManager.get(mObj);//clipboardmanager obj
        
        System.out.println("system hook = " + (o));
        
        ClipboardManagerHook hook = new ClipboardManagerHook(o);
        Object proxyClipManager = hook.getProxy(o.getClass());
        mClipboardManager.set(o,proxyClipManager);
        return method.invoke(mObj, args);
    }



    public Object getProxy(Class<?> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }
}

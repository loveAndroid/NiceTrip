package com.example.small.clipbord;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;


public class BinderHookHelper {

    public final static String CLIPBOARD_SERVICE = "clipboard";



    public static void hook(Context context) throws  Exception {

        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object thread = getActivityThread(context, activityThreadClass);
        
        Method systemContextMethod = activityThreadClass.getMethod("getSystemContext");
        Object o = systemContextMethod.invoke(thread);// o == ContextImpl 
        
        SystemContextHook hook = new SystemContextHook(o);
        Object proxyContextImpl = hook.getProxy(o.getClass());
        System.out.println("proxyContextImpl = " + proxyContextImpl);


        Field mSystemContext = activityThreadClass.getField("mSystemContext");
        mSystemContext.setAccessible(true);
        mSystemContext.set(thread,proxyContextImpl);
    }
    
    public static Object getActivityThread(Context context, Class<?> activityThread) {
        try {
            // ActivityThread.currentActivityThread()
            Method m = activityThread.getMethod("currentActivityThread", new Class[0]);
            m.setAccessible(true);
            Object thread = m.invoke(null, new Object[0]);
            if (thread != null) return thread;

            // context.@mLoadedApk.@mActivityThread
            Class<? extends Context> aClass = context.getClass();
            Field mLoadedApk = aClass.getField("mLoadedApk");
            mLoadedApk.setAccessible(true);
            Object apk = mLoadedApk.get(context);
            Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
            mActivityThreadField.setAccessible(true);
            return mActivityThreadField.get(apk);
        } catch (Throwable ignore) {
        	System.out.println(" get thread  exception = " + ignore.getLocalizedMessage());
        }

        return null;
    }











}

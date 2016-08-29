package com.example.injectactivity;

import java.lang.reflect.Field;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import net.wequick.small.internal.InstrumentationInternal;

public class InjectUtils {

	private static Instrumentation sHostInstrumentation;
	private static Instrumentation sBundleInstrumentation;
//	private static HashMap<String, ActivityInfo> acHashMap = new HashMap<>();
	
	static Context mContext;

	public static void inject(Context context) {

		mContext = context;
		
		
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ActivityInfo[] activities = packageInfo.activities;

		if(activities != null) {
			for(ActivityInfo info : activities) {
//				acHashMap.put(info.name, info);
			}
		}
		
		if (sHostInstrumentation == null) {
			try {
				// Inject instrumentation
				final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
				Object thread = ReflectAccelerator.getActivityThread(context, activityThreadClass);
				Field field = activityThreadClass.getDeclaredField("mInstrumentation");
				field.setAccessible(true);
				sHostInstrumentation = (Instrumentation) field.get(thread);
				Instrumentation wrapper = new InstrumentationWrapper();
				field.set(thread, wrapper);
				if (!sHostInstrumentation.getClass().getName().equals("android.app.Instrumentation")) {
					sBundleInstrumentation = wrapper; // record for later
														// replacement
				}

				if (context instanceof Activity) {
					field = Activity.class.getDeclaredField("mInstrumentation");
					field.setAccessible(true);
					field.set(context, wrapper);
				}

				// Inject handler
				field = activityThreadClass.getDeclaredField("mH");
				field.setAccessible(true);
				Handler ah = (Handler) field.get(thread);
				field = Handler.class.getDeclaredField("mCallback");
				field.setAccessible(true);
				field.set(ah, new ActivityThreadHandlerCallback());
			} catch (Exception ignored) {
				ignored.printStackTrace();
				// Usually, cannot reach here
			}
		}
	}

	private static class ActivityThreadHandlerCallback implements Handler.Callback {

		private static final int LAUNCH_ACTIVITY = 100;

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what != LAUNCH_ACTIVITY){
				return false;
			} else {
				//msg.what == launch_activity
				System.out.println("msg.what == launch activity");
				
				Object/*ActivityClientRecord*/ r = msg.obj;
				Intent intent = ReflectAccelerator.getIntent(r);
				ComponentName component = intent.getComponent();
				
				if(component.getClassName().equals(AA.name)) {
					System.out.println(intent.getComponent().toString());
		            // Replace with the REAL activityInfo
//		            ActivityInfo targetInfo = acHashMap.get(A.name);
//		            ReflectAccelerator.setActivityInfo(r, targetInfo);
		            intent.setClassName(component.getPackageName(), A.name);
		            System.out.println(r);
		            System.out.println(r);
				}
				return false;
			}
		}
	}
	
	private static void wrapIntent(Intent intent) {
		String stubClazz = AA.name;
		intent.setComponent(new ComponentName(App.context, stubClazz ));
    }

	/**
	 * Class for redirect activity from Stub(AndroidManifest.xml) to
	 * Real(Plugin)
	 */
	private static class InstrumentationWrapper extends Instrumentation implements InstrumentationInternal {

		public InstrumentationWrapper() {
		}

		/**
		 * @Override V21+ Wrap activity from REAL to STUB
		 */
		public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
				Intent intent, int requestCode, android.os.Bundle options) {
			System.out.println("exec .. top ");
			wrapIntent(intent);
			return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
					requestCode, options);
		}

		/**
		 * @Override V20- Wrap activity from REAL to STUB
		 */
		public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
				Intent intent, int requestCode) {
			System.out.println("exec .. bottom ");
			return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
					requestCode);
		}

		@Override
		/** Prepare resources for REAL */
		public void callActivityOnCreate(Activity activity, android.os.Bundle icicle) {
			System.out.println("callActivityOnCreate");
			sHostInstrumentation.callActivityOnCreate(activity, icicle);
		}

		@Override
		public void callActivityOnStop(Activity activity) {
			System.out.println("callActivityOnStop");
			sHostInstrumentation.callActivityOnStop(activity);
		}

		@Override
		public void callActivityOnDestroy(Activity activity) {
			System.out.println("callActivityOnDestroy");
			sHostInstrumentation.callActivityOnDestroy(activity);
		}

	}

}

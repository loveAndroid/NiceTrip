package com.example.injectactivity.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.example.injectactivity.Launcher;
import com.example.injectactivity.external.IActInject;
import com.example.injectactivity.internal.InstrumentationInternal;
import com.example.injectactivity.obj.LoadApk;
import com.example.injectactivity.stub.AAStubAct;
import com.example.injectactivity.util.ReflectAccelerator;

/**
 * Class for redirect activity from Stub(AndroidManifest.xml) to Real(Plugin)
 */
public class InstrumentationWrapper extends Instrumentation implements InstrumentationInternal {

	private Instrumentation sHostInstrumentation;
	private static List<LoadApk> mLoadApks;
	private Context mContext;
	private Instrumentation sBundleInstrumentation;

	public InstrumentationWrapper(Instrumentation sHostInstrumentation, Context context) {
		this.mLoadApks = Launcher.getInstance().getLoadApks();
		this.sHostInstrumentation = sHostInstrumentation;
		this.mContext = context;
	}

	public InstrumentationWrapper() {
	}

	/**
	 * @Override V21+ Wrap activity from REAL to STUB
	 */
	public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
			Intent intent, int requestCode, android.os.Bundle options) {
		wrapIntent(intent);
		return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
				requestCode, options);
	}

	/**
	 * @Override V20- Wrap activity from REAL to STUB
	 */
	public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
			Intent intent, int requestCode) {
		wrapIntent(intent);
		return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
				requestCode);
	}

	@Override
	public void callActivityOnCreate(Activity activity, android.os.Bundle icicle) {
		sHostInstrumentation.callActivityOnCreate(activity, icicle);
	}

	@Override
	public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent,
			ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance)
					throws InstantiationException, IllegalAccessException {
		Activity newActivity = super.newActivity(clazz, context, token, application, intent, info, title, parent, id,
				lastNonConfigurationInstance);
		injectResource(newActivity);
		return newActivity;
	}

	@Override
	public Activity newActivity(ClassLoader cl, String className, Intent intent)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Activity newActivity = super.newActivity(cl, className, intent);
		injectResource(newActivity);
		return newActivity;
	}

	private void injectResource(Activity activity) {

		checkInstrumentation(activity);

		try {
			if (activity != null && activity instanceof IActInject) {
				if(activity.getResources() == null) {
					LoadApk mPluginApk = getTargetApk(activity);
					if (mPluginApk != null) {
						Method setPluginResources = activity.getClass().getMethod("setPluginResources", Resources.class, AssetManager.class);
						setPluginResources.setAccessible(true);
						setPluginResources.invoke(activity, mPluginApk.resources, mPluginApk.assetManager);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("injectResource exceptipn = " + e.getMessage());
		}
	}

	private void checkInstrumentation(Activity activity) {
		if (sBundleInstrumentation != null) {
            try {
                Field f = Activity.class.getDeclaredField("mInstrumentation");
                f.setAccessible(true);
                Object instrumentation = f.get(activity);
                if (instrumentation != sBundleInstrumentation) {
                    f.set(activity, sBundleInstrumentation);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
	}

	private LoadApk getTargetApk(String packageName) {
		if (mLoadApks != null && mLoadApks.size() > 0) {
			for (int i = 0 ;i < mLoadApks.size();i++) {
				LoadApk loadApk  = mLoadApks.get(i);
				if (loadApk.packageName.equalsIgnoreCase(packageName)) {
					return loadApk;
				}
			}
		}
		return null;
	}

	private LoadApk getTargetApk(Activity actInject) {
		String packageName = getTargetPkgName(actInject);
		return getTargetApk(packageName);
	}

	/**
	 * reflect the method getPluginPkgName to get the load apks package name
	 */
	private String getTargetPkgName(Activity actInject) {
		try {
			Method pkgNameMethod = actInject.getClass().getMethod("getPluginPkgName");
			Object invoke = pkgNameMethod.invoke(actInject);
			return invoke.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * replace the target launch activity to a stub AA.class
	 */
	private void wrapIntent(Intent intent) {
		// real class name
		intent.addCategory(Launcher.STUB_PREFIX_STRING + intent.getComponent().getClassName());
		String stubClazz = getSuitableStubAct();
		intent.setComponent(new ComponentName(mContext, stubClazz));
	}

	/**
	 * by the target activity`s info i.g : intentFilter , launch mode ... return
	 * a suitable stub activity`s name
	 */
	private static String getSuitableStubAct() {
		// TODO
		return AAStubAct.name;
	}

	public static class ActivityThreadHandlerCallback implements Handler.Callback {
		private static final int LAUNCH_ACTIVITY = 100;

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what != LAUNCH_ACTIVITY) {
				return false;
			} else {
				Object/* ActivityClientRecord */ r = msg.obj;
				Intent intent = ReflectAccelerator.getIntent(r);
				if (isStub(intent)) {
					// Replace with the REAL activityInfo
					String realClsName = unWrapIntent(intent);
					if (TextUtils.isEmpty(realClsName))
						return false;
					intent.setClassName(mLoadApks.get(0).packageName, realClsName);
					ReflectAccelerator.setIntent(r, intent);
				}
				return false;
			}
		}
	}

	private boolean isStub(ComponentName component) {
		return component.getClassName().equals(AAStubAct.name);
	}

	private static boolean isStub(Intent intent) {
		if (intent == null)
			return false;
		Set<String> categories = intent.getCategories();
		if (categories != null && categories.size() > 0) {
			for (String key : categories) {
				if (key.startsWith(Launcher.STUB_PREFIX_STRING)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * return the real class name;
	 */
	private static String unWrapIntent(Intent intent) {
		Set<String> categories = intent.getCategories();
		if (categories != null && categories.size() > 0) {
			for (String key : categories) {
				if (key.startsWith(Launcher.STUB_PREFIX_STRING)) {
					categories.remove(key);
					return key.substring(1);
				}
			}
		}
		return null;
	}

	public void setBundleInstrumentation(Instrumentation sBundleInstrumentation) {
		this.sBundleInstrumentation = sBundleInstrumentation;
	}

}

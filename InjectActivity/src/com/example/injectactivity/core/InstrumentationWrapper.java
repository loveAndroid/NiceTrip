package com.example.injectactivity.core;

import java.util.List;
import java.util.Set;

import com.example.injectactivity.Launcher;
import com.example.injectactivity.external.IActInject;
import com.example.injectactivity.internal.InstrumentationInternal;
import com.example.injectactivity.obj.LoadApk;
import com.example.injectactivity.stub.AAStubAct;
import com.example.injectactivity.util.ReflectAccelerator;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

/**
 * Class for redirect activity from Stub(AndroidManifest.xml) to Real(Plugin)
 */
public class InstrumentationWrapper extends Instrumentation implements InstrumentationInternal {

	private Activity mTargetAct;
	private Instrumentation sHostInstrumentation;
	private static List<LoadApk> mLoadApks;
	private Context mContext;

	public InstrumentationWrapper(List<LoadApk> mLoadApks, Instrumentation sHostInstrumentation, Context context) {
		this.mLoadApks = mLoadApks;
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
		this.mTargetAct = target;
		wrapIntent(intent);
		return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
				requestCode, options);
	}

	/**
	 * @Override V20- Wrap activity from REAL to STUB
	 */
	public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
			Intent intent, int requestCode) {
		this.mTargetAct = target;
		wrapIntent(intent);
		return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target, intent,
				requestCode);
	}

	@Override
	/** Prepare resources for REAL */
	public void callActivityOnCreate(Activity activity, android.os.Bundle icicle) {
		injectResource(activity);
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
		try {
			if (activity != null && activity instanceof IActInject) {
				IActInject actInject = (IActInject) activity;
				LoadApk mPluginApk = getTargetApk(actInject);
				if (mPluginApk != null && mPluginApk.resources == null) {
					Resources resources = mTargetAct.getResources();
					Resources pluginResources = new Resources(mPluginApk.assetManager, resources.getDisplayMetrics(),
							resources.getConfiguration());
					mPluginApk.resources = pluginResources;
				}
				actInject.setResources(mPluginApk.resources, mPluginApk.assetManager);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private LoadApk getTargetApk(IActInject actInject) {
		
		return mLoadApks.get(0);
//		String packageName = actInject.getPackageName();
//		if (mLoadApks != null && mLoadApks.size() > 0) {
//			for (LoadApk loadApk : mLoadApks) {
//				if (loadApk.packageName.equals(packageName)) {
//					return loadApk;
//				}
//			}
//		}
//		return null;
	}

	@Override
	public void callActivityOnStop(Activity activity) {
		sHostInstrumentation.callActivityOnStop(activity);
	}

	@Override
	public void callActivityOnDestroy(Activity activity) {
		sHostInstrumentation.callActivityOnDestroy(activity);
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

}

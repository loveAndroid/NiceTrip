package com.example.injectactivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.example.injectactivity.external.IActInject;
import com.example.injectactivity.internal.InstrumentationInternal;
import com.example.injectactivity.obj.LoadApk;
import com.example.injectactivity.stub.AAStubAct;
import com.example.injectactivity.util.FileUtils;
import com.example.injectactivity.util.FixDexUtils;
import com.example.injectactivity.util.ReflectAccelerator;

public class Launcher {

	private static final String STUB_PREFIX_STRING = ">";
	private static Instrumentation sHostInstrumentation;
	private static Instrumentation sBundleInstrumentation;
	static Context mContext;
	LoadApk mPluginApk;

	private static Launcher launcher = new Launcher();

	public static Launcher getInstance() {
		return launcher;
	}

	private Launcher() {
	}

	public void launch(Context context) {
		mContext = context;
		preLauncher(context);
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

	/**
	 * load a apk file from fileSystem
	 */
	private void preLauncher(Context context) {

		LoadApk pluginApk = new LoadApk();

		File pluginFile = Environment.getExternalStorageDirectory();
		File file = new File(pluginFile, "pluginA.apk");
		String path = file.getAbsolutePath();

		PackageInfo packageInfo = context.getPackageManager()
				.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
		ActivityInfo[] activityInfos = packageInfo.activities;

		ActivityInfo mainPlauginActivityInfo = null;
		HashMap<String, ActivityInfo> mLoadActivities = new HashMap<>();
		if (activityInfos != null && activityInfos.length > 0) {
			mainPlauginActivityInfo = activityInfos[0];
			for (ActivityInfo info : activityInfos) {
				if (info != null) {
					String clsName = info.name;
					mLoadActivities.put(clsName, info);
				}
			}
		}

		pluginApk.sourcePath = path;
		pluginApk.mainActivity = mainPlauginActivityInfo;
		pluginApk.loadActivities = mLoadActivities;
		pluginApk.packageName = packageInfo.packageName;

		inject(context, file, pluginApk);
	}

	private void inject(Context context, File pluginFile, LoadApk loadApk) {
		String sourceFile = pluginFile.getAbsolutePath();
		String targetFileDir = context.getDir("odex_", Context.MODE_PRIVATE).getAbsolutePath();
		String targetFileName = targetFileDir + File.separator + pluginFile.getName();
		File file = new File(targetFileDir, pluginFile.getName());
		try {
			FileUtils.copyFile(sourceFile, targetFileName);
			FixDexUtils.loadFixDex(context, file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		AssetManager pluginAssetManager = ReflectAccelerator.newAssetManager();
		ReflectAccelerator.addAssetPath(pluginAssetManager, targetFileName);

		loadApk.assetManager = pluginAssetManager;
		loadApk.dexPath = file.getAbsolutePath();
		loadApk.dexPath = targetFileName;
		this.mPluginApk = loadApk;
	}

	private class ActivityThreadHandlerCallback implements Handler.Callback {

		private static final int LAUNCH_ACTIVITY = 100;

		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what != LAUNCH_ACTIVITY) {
				return false;
			} else {
				Object/* ActivityClientRecord */r = msg.obj;
				Intent intent = ReflectAccelerator.getIntent(r);
				ComponentName component = intent.getComponent();
				if (isStub(intent)) {
					// Replace with the REAL activityInfo
					String realClsName = unWrapIntent(intent);
					if (TextUtils.isEmpty(realClsName))
						return false;
					intent.setClassName(mPluginApk.packageName, realClsName);
					ReflectAccelerator.setIntent(r, intent);
				}
				return false;
			}
		}
	}
	
	private boolean isStub(ComponentName component) {
		return component.getClassName().equals(AAStubAct.name);
	}
	
	private boolean isStub(Intent intent) {
		if(intent == null) return false;
		Set<String> categories = intent.getCategories();
		if(categories != null && categories.size() > 0) {
			for(String key : categories) {
				if(key.startsWith(STUB_PREFIX_STRING)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * by the target activity`s info i.g : intentFilter , launch mode ...
	 * return a suitable stub activity`s name
	 */
	private static String getSuitableStubAct() {
		//TODO 
		return AAStubAct.name;
	}
	
	/**
	 * replace the target launch activity to a stub AA.class
	 */
	private static void wrapIntent(Intent intent) {
		// real class name
		intent.addCategory(STUB_PREFIX_STRING + intent.getComponent().getClassName());
		String stubClazz = getSuitableStubAct();
		intent.setComponent(new ComponentName(DynamicApplication.context, stubClazz));
	}
	
	/**
	 * return the real class name;
	 */
	private static String unWrapIntent(Intent intent) {
		Set<String> categories = intent.getCategories();
		if (categories != null && categories.size() > 0) {
			for (String key : categories) {
				if (key.startsWith(STUB_PREFIX_STRING)) {
					categories.remove(key);
					return key.substring(1);
				}
			}
		}
		return null;
	}

	/**
	 * Class for redirect activity from Stub(AndroidManifest.xml) to
	 * Real(Plugin)
	 */
	private class InstrumentationWrapper extends Instrumentation implements InstrumentationInternal {
		
		private Activity mTargetAct;
		
		public InstrumentationWrapper() {
		}
		
		/**
		 * @Override V21+ Wrap activity from REAL to STUB
		 */
		public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
				Intent intent, int requestCode, android.os.Bundle options) {
			this.mTargetAct = target;
			wrapIntent(intent);
			return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target,
					intent, requestCode, options);
		}
		
		/**
		 * @Override V20- Wrap activity from REAL to STUB
		 */
		public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
				Intent intent, int requestCode) {
			this.mTargetAct = target;
			wrapIntent(intent);
			return ReflectAccelerator.execStartActivity(sHostInstrumentation, who, contextThread, token, target,
					intent, requestCode);
		}

		@Override
		/** Prepare resources for REAL */
		public void callActivityOnCreate(Activity activity, android.os.Bundle icicle) {
			injectResource(activity);
			sHostInstrumentation.callActivityOnCreate(activity, icicle);
		}

		@Override
		public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application,
				Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id,
				Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
			Activity newActivity = super.newActivity(clazz, context, token, application, intent, info, title, parent,
					id, lastNonConfigurationInstance);
			injectResource(newActivity);
			return newActivity;
		}

		@Override
		public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException,
				IllegalAccessException, ClassNotFoundException {
			Activity newActivity = super.newActivity(cl, className, intent);
			injectResource(newActivity);
			return newActivity;
		}

		private void injectResource(Activity activity) {
			try {
				if (activity != null && activity instanceof IActInject) {
					IActInject actInject = (IActInject) activity;
					if (mPluginApk.resources == null) {
						Resources resources = mTargetAct.getResources();
						Resources pluginResources = new Resources(mPluginApk.assetManager,
								resources.getDisplayMetrics(), resources.getConfiguration());
						mPluginApk.resources = pluginResources;
					}
					actInject.setResources(mPluginApk.resources, mPluginApk.assetManager);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void callActivityOnStop(Activity activity) {
			sHostInstrumentation.callActivityOnStop(activity);
		}

		@Override
		public void callActivityOnDestroy(Activity activity) {
			sHostInstrumentation.callActivityOnDestroy(activity);
		}
	}

}

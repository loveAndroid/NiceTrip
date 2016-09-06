package com.example.injectactivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.injectactivity.core.InstrumentationWrapper;
import com.example.injectactivity.obj.LoadApk;
import com.example.injectactivity.util.FileUtils;
import com.example.injectactivity.util.FixDexUtils;
import com.example.injectactivity.util.ReflectAccelerator;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.os.Handler;

public class Launcher {

	public static final String STUB_PREFIX_STRING = ">";
	private static Instrumentation sHostInstrumentation;
	private static Instrumentation sBundleInstrumentation;
	private List<LoadApk> mLoadApks = new ArrayList<>();
	
	private static Launcher launcher = new Launcher();
	public static Launcher getInstance() {
		return launcher;
	}

	private Launcher() {
	}

	public void launch(Context context) {
		preLauncher(context);
		if (sHostInstrumentation == null) {
			try {
				// Inject instrumentation
				final Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
				Object thread = ReflectAccelerator.getActivityThread(context, activityThreadClass);
				Field field = activityThreadClass.getDeclaredField("mInstrumentation");
				field.setAccessible(true);
				sHostInstrumentation = (Instrumentation) field.get(thread);
				InstrumentationWrapper wrapper = new InstrumentationWrapper(sHostInstrumentation,context);
				field.set(thread, wrapper);
				if (!sHostInstrumentation.getClass().getName().equals("android.app.Instrumentation")) {
					sBundleInstrumentation = wrapper; // record for later
														// replacement
					wrapper.setBundleInstrumentation(sBundleInstrumentation);
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
				field.set(ah, new InstrumentationWrapper.ActivityThreadHandlerCallback());
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

		if (!file.exists()) {
			return;
		}

		String path = file.getAbsolutePath();

		PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
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
		//instance assetManager
		AssetManager pluginAssetManager = ReflectAccelerator.newAssetManager();
		ReflectAccelerator.addAssetPath(pluginAssetManager, targetFileName);
		//instance resource
		Resources superResources = context.getResources();
		Resources pluginResources = new Resources(pluginAssetManager, superResources.getDisplayMetrics(),
				superResources.getConfiguration());

		loadApk.resources = pluginResources;
		loadApk.assetManager = pluginAssetManager;
		loadApk.dexPath = file.getAbsolutePath();
		loadApk.dexPath = targetFileName;
		this.mLoadApks.add(loadApk);
	}

	public List<LoadApk> getLoadApks() {
		return mLoadApks;
	}

}

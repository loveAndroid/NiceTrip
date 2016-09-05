package com.example.injectactivity.external;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * default class for plugin activity
 */
public abstract class InjectBaseAct extends Activity implements IActInject {

	private Resources mResources;
	private AssetManager mAssetManager;
	
	@Override
	public void setPluginResources(Resources resources, AssetManager assetManager) {
		this.mResources = resources;
		this.mAssetManager = assetManager;
		System.out.println("host ... setPluginResources = " + (resources == null) + " , " + (assetManager == null));
	}

	@Override
	public Resources getResources() {
		System.out.println("host ... getResources = " + (mResources == null));
		return mResources;
	}

	@Override
	public AssetManager getAssets() {
		System.out.println("host ... getAssets = " + (mAssetManager == null));
		return mAssetManager;
	}
	
	@Override
	public String getPluginPkgName() {
		return "com.example.injectplugin.module";
	}
}

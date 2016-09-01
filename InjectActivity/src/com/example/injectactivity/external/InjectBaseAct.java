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
	public void setResources(Resources resources, AssetManager assetManager) {
		this.mResources = resources;
		this.mAssetManager = assetManager;
	}

	@Override
	public Resources getResources() {
		return mResources == null ? super.getResources() : mResources;
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager == null ? super.getAssets() : mAssetManager;
	}
	
	@Override
	public String getPackageName() {
		return super.getPackageName();
	}

}

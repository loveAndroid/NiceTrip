package com.example.injectactivity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class InjectBaseAct extends Activity implements IActInject {

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

}

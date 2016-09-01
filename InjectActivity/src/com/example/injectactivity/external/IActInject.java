package com.example.injectactivity.external;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 *  Extract the public interface , that the plugin activity must inherit
 */
public interface IActInject {
	void setResources(Resources resources,AssetManager assetManager);
//	String getPackageName();
}

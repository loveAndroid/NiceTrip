package azir.dynamic.load.external;

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
	}

	@Override
	public Resources getResources() {
		return mResources;
	}

	@Override
	public AssetManager getAssets() {
		return mAssetManager;
	}
	
	@Override
	public String getPluginPkgName() {
		return "com.example.injectplugin.module";
	}
}

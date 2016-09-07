package com.example.injectplugin.module;

import android.os.Bundle;
import android.widget.Toast;

import azir.dynamic.load.external.InjectBaseAct;
import azir.dynamic.load.external.PluginAccessAble;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Method;

public class MainActivity extends InjectBaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        try {
            Class<?> aClass = Class.forName("azir.dynamic.load.external.impl.PluginAccessAbleImpl");
            Object obj = aClass.newInstance();
            Method getPluginAccessAbleImpl = aClass.getMethod("getInstance");
            Object invoke = getPluginAccessAbleImpl.invoke(obj);

            if (invoke != null && invoke instanceof PluginAccessAble) {
                PluginAccessAble accessAble = (PluginAccessAble) invoke;
                String userName = accessAble.getUserName();
                Toast.makeText(this,"name = " + userName,Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
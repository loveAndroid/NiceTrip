package com.example.injectplugin.module;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.injectactivity.external.InjectBaseAct;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends InjectBaseAct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

//        try {
//            Class<?> launcherClazz = Class.forName("com.example.injectactivity.Launcher");
//            Method methodInstance = launcherClazz.getMethod("getInstance", new Class[0]);
//            Object objLauncher = methodInstance.invoke(null, new Object[0]);
//
//            Method getLoadApks = launcherClazz.getMethod("getLoadApks", new Class[0]);
//            List loadApks = (List) getLoadApks.invoke(objLauncher, new Object[0]);
//
//            Toast.makeText(this,"size = " + loadApks.size(), Toast.LENGTH_LONG).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

}
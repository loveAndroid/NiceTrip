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

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        mViewPager = (ViewPager) findViewById(R.id.mainPager);
        mViewPager.setAdapter(new PagerAdapters());


        try {
            Class<?> launcherClazz = Class.forName("com.example.injectactivity.Launcher");
            Method methodInstance = launcherClazz.getMethod("getInstance", new Class[0]);
            Object objLauncher = methodInstance.invoke(null, new Object[0]);

            Method getLoadApks = launcherClazz.getMethod("getLoadApks", new Class[0]);
            List loadApks = (List) getLoadApks.invoke(objLauncher, new Object[0]);

            Toast.makeText(this,"size = " + loadApks.size(), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    class PagerAdapters extends PagerAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(MainActivity.this);
            ImageLoader.getInstance().displayImage("https://www.baidu.com/img/bd_logo1.png",imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
        }
    }



}
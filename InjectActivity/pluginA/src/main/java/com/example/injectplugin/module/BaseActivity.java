package com.example.injectplugin.module;

import azir.dynamic.load.external.InjectBaseAct;

/**
 * Created by Kongxs on 2016- 09-08.
 */
public class BaseActivity extends InjectBaseAct {
    @Override
    public String getPluginPkgName() {
        return "com.example.injectplugin.module";
    }
}

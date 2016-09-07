package azir.dynamic.load.external.impl;

import azir.dynamic.load.external.PluginAccessAble;

/**
 * Created by Kongxs on 2016- 09-07.
 */
public class PluginAccessAbleImpl implements PluginAccessAble {

    private PluginAccessAbleImpl getInstance(){
        return new PluginAccessAbleImpl();
    }

    @Override
    public String getUserName() {
        return "host name";
    }
}

package com.stdnull.v2api.injection.components;

import com.stdnull.v2api.injection.ActivityScope;
import com.stdnull.v2api.injection.modules.V2MainModule;
import com.stdnull.v2api.ui.V2MainFragment;

import dagger.Component;

/**
 * Created by iamwent on 2016/3/8.
 */
@ActivityScope
@Component(modules = {V2MainModule.class})
public interface V2MainComponent {
    void inject(V2MainFragment v2MainFragment);
}

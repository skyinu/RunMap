package com.stdnull.v2api.injection.modules;

import android.content.Context;

import com.stdnull.v2api.injection.ActivityScope;
import com.stdnull.v2api.ui.V2MainFragment;
import com.stdnull.v2api.ui.uibehaviour.IV2MainFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class V2MainModule {
    private final V2MainFragment mV2MainFragment;

    public V2MainModule(V2MainFragment v2MainFragment) {
        this.mV2MainFragment = v2MainFragment;
    }

    @Provides
    @ActivityScope
    IV2MainFragment provideIV2MainFragment() {
        return mV2MainFragment;
    }

    @Provides
    @ActivityScope
    Context provideContext(){
        return mV2MainFragment.getActivity();
    }

}

package com.stdnull.v2api.ui.uibehaviour;

import android.content.Context;

import com.stdnull.v2api.mode.V2ExBean;

import java.util.List;

/**
 * Created by chen on 2017/8/20.
 */

public interface IV2MainFragment {
    void showContent(List<V2ExBean> content);
    Context getActivityContext();
}

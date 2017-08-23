package com.stdnull.v2api.model;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2017/8/20.
 */

public class V2MainFragModel {
    private static final String KEY_V2EXBEAN = "KEY_V2EXBEAN";
    private List<V2ExBean> mContentListModel = new ArrayList<>();

    public List<V2ExBean> getContentListModel() {
        return mContentListModel;
    }

    public void addContentListModel(List<V2ExBean> contentListModel) {
        if(contentListModel != null) {
            this.mContentListModel.addAll(contentListModel);
        }
    }

    public boolean isModelEmpty(){
        return mContentListModel.isEmpty() ;
    }

    public void clearModel(){
        mContentListModel.clear();
    }
    public void save(Bundle bundle){
        bundle.putParcelableArrayList(KEY_V2EXBEAN, (ArrayList<? extends Parcelable>) mContentListModel);
    }
    public boolean restore(Bundle bundle){
        if(bundle == null){
            return false;
        }
        mContentListModel = bundle.getParcelableArrayList(KEY_V2EXBEAN);
        return mContentListModel != null && !mContentListModel.isEmpty();
    }
}

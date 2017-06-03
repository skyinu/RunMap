package com.stdnull.runmap.ui.activity;

/**
 * Created by chen on 2017/6/3.
 */

public interface IReviewActivity {
    void showEmptyView();
    void updateDateTitle(String date);
    void setLeftArrowVisibility(int visibility);
    void updateArrowState(int direction, int position, int count);
}

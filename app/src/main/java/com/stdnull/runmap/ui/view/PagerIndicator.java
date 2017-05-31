package com.stdnull.runmap.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 滑动容的标题指示器UI控件
 * Created by chen on 2016/10/8.
 */

public class PagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {
    /**
     * 每个tab的标题
     */
    private List<String> mTitles;
    private ViewPager mParentPager;
    /**
     * 指示器绘制的起点坐标
     */
    private float mStartX = 0;
    /**
     * 指示器的画笔
     */
    private Paint mIndicatorPaint;

    public PagerIndicator(Context context) {
        this(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setColor(Color.YELLOW);
        mIndicatorPaint.setStrokeWidth(10);
    }

    /**
     * 设置每个tab的标题
     *
     * @param mTitle
     */
    public void setTitle(String[] mTitle) {
        if (mTitles == null) {
            mTitles = new ArrayList<>();
        }
        mTitles.addAll(Arrays.asList(mTitle));
        removeAllViews();
        addNewTitleView(mTitle);
    }


    private void addNewTitleView(String titles[]) {
        for (String title : titles) {
            TextView tv = generateTitleView(title);
            addView(tv);
        }
    }

    private TextView generateTitleView(String title) {
        TextView tv = new TextView(getContext());
        tv.setGravity(Gravity.CENTER);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tv.setText(title);
        tv.setLayoutParams(lp);
        tv.setOnClickListener(this);
        tv.setSingleLine();
        return tv;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        View child = getChildAt(0);
        canvas.drawLine(mStartX, child.getHeight(), child.getWidth() + mStartX, child.getHeight(), mIndicatorPaint);
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ViewParent parent = getParent();
        if (!(parent instanceof ViewPager)) {
            throw new IllegalStateException("PagerIndicator must be a direct child of a ViewPager.");
        }
        ((ViewPager.LayoutParams) getLayoutParams()).isDecor = true;
        mParentPager = (ViewPager) parent;
        mParentPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mParentPager = null;
    }

    @Override
    public void setOrientation(int orientation) {
        throw new UnsupportedOperationException("can't modify Orientation");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View child = getChildAt(position);
        mStartX = positionOffset * child.getWidth() + child.getX();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        View child = getChildAt(position);
        mStartX = position + child.getWidth();
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            if (v == getChildAt(i)) {
                mParentPager.setCurrentItem(i, true);
            }
        }
    }
}

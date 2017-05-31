package com.stdnull.runmap.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import com.stdnull.runmap.R;

/**
 * 我的页面功能条目UI
 * Created by chen on 2016/3/20.
 */
public class SettingItemView extends Button {
    private Bitmap mStartImage;
    private Rect mImageRegion;
    private Rect mSrc;
    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.SettingItemView);
        mStartImage=((BitmapDrawable)ta.getDrawable(R.styleable.SettingItemView_view_image)).getBitmap();
        ta.recycle();
        int width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,45,getResources().getDisplayMetrics());
        int offset= (int) (5*getResources().getDisplayMetrics().density);
        mImageRegion=new Rect(offset,offset,width-offset,width-offset);
        mSrc=new Rect(0,0,mStartImage.getWidth(),mStartImage.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mStartImage,mSrc,mImageRegion,null);
    }
}

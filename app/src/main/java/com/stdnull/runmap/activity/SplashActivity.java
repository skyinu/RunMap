package com.stdnull.runmap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.stdnull.runmap.R;

/**
 * 系统欢迎页
 * Created by chen on 2017/1/23.
 */

public class SplashActivity extends BaseActivity {
    private ImageView mSplashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        init();
    }
    protected void init() {
        mSplashImage = (ImageView) findViewById(R.id.splash_bitmap);
        mSplashImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
        return;
    }
}

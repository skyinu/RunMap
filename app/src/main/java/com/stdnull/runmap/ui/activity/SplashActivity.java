package com.stdnull.runmap.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.skyinu.annotations.BindView;
import com.skyinu.gradlebutterknife.GradleButterKnife;
import com.stdnull.baselib.BaseActivity;
import com.stdnull.runmap.R;
import com.stdnull.runmap.R2;
import com.stdnull.runmap.presenter.action.ISplashPresenter;
import com.stdnull.runmap.presenter.actionImpl.SplashPresenterImpl;
import com.stdnull.runmap.ui.uibehavior.ISplashActivity;

/**
 * 系统欢迎页
 * Created by chen on 2017/1/23.
 */

public class SplashActivity extends BaseActivity implements ISplashActivity {
    private ISplashPresenter mSplashPresenter;
    @BindView(R2.id.splash_iv_container)
    SimpleDraweeView splashDraweee;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GradleButterKnife.bind(this);
        Uri uri = Uri.parse("asset:///splash.webp");
        mSplashPresenter = new SplashPresenterImpl(this);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .setControllerListener(new BaseControllerListener<ImageInfo>(){
                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        mSplashPresenter.startCountDown(1);
                    }
                })
                .build();
        splashDraweee.setController(controller);
    }
    @Override
    public void onCountDownFinished() {
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

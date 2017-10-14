package com.stdnull.runmap.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import com.facebook.animated.webp.WebPFrame;
import com.facebook.animated.webp.WebPImage;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;
import com.facebook.imagepipeline.animated.impl.AnimatedDrawableBackendImpl;
import com.facebook.imagepipeline.animated.impl.AnimatedImageCompositor;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;
import com.facebook.imagepipeline.image.ImageInfo;
import com.stdnull.baselib.BaseActivity;
import com.stdnull.baselib.utils.SystemUtils;
import com.stdnull.runmap.R;
import com.stdnull.runmap.R2;
import com.stdnull.runmap.presenter.action.ISplashPresenter;
import com.stdnull.runmap.presenter.actionImpl.SplashPresenterImpl;
import com.stdnull.runmap.ui.uibehavior.ISplashActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import butterknife.BindView;
import butterknife.ButterKnife;

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
        ButterKnife.bind(this);
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

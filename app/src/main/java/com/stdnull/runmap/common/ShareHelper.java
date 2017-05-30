package com.stdnull.runmap.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.stdnull.runmap.R;
import com.stdnull.runmap.modules.map.AmLocationManager;
import com.stdnull.runmap.modules.map.OnCaptureListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * 微信分享操作类
 * Created by chen on 2017/2/10.
 */

public class ShareHelper implements View.OnClickListener, OnCaptureListener{
    private static final ShareHelper mInstance = new ShareHelper();
    private IWXAPI mWxApi;
    private PopupWindow mPopupWindow;
    private int shareFlag;

    private ShareHelper(){}

    public static ShareHelper getInstance(){
        return mInstance;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share_to_circle:
            case R.id.btn_share_to_friend:
                share(v.getId());
                break;
        }

    }

    public void initWXShare(Context context){
        mWxApi = WXAPIFactory.createWXAPI(context,RMConfiguration.WEIXIN_APP_ID,true);
        CFLog.e("Share","register = " + mWxApi.registerApp(RMConfiguration.WEIXIN_APP_ID));
        AmLocationManager.getInstance().setCaptureListener(this);
    }

    private void share(int id){
        AmLocationManager.getInstance().captureMap();
        mPopupWindow.dismiss();
        shareFlag = id;

    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public PopupWindow showShareView(Activity context ){
        mPopupWindow = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.sharelayout,null);
        view.findViewById(R.id.btn_share_to_circle).setOnClickListener(this);
        view.findViewById(R.id.btn_share_to_friend).setOnClickListener(this);
        mPopupWindow.setContentView(view);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(context.findViewById(android.R.id.content), Gravity.BOTTOM,0,0);
        return mPopupWindow;
    }

    @Override
    public void onCaptureFinished(Bitmap bitmap, int status) {
        WXImageObject imageObject = new WXImageObject(bitmap);

        WXMediaMessage mediaMessage = new WXMediaMessage();
        mediaMessage.mediaObject = imageObject;
        Bitmap thumb = Bitmap.createScaledBitmap(bitmap,50,50,true);
        bitmap.recycle();
        mediaMessage.thumbData = bmpToByteArray(thumb,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = mediaMessage;
        if(shareFlag == R.id.btn_share_to_friend){
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        else{
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        CFLog.e("Share","send = "+ mWxApi.sendReq(req));
    }
}

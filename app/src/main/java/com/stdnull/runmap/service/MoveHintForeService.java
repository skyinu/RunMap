package com.stdnull.runmap.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import com.stdnull.runmap.R;
import com.stdnull.runmap.common.CFLog;

/**
 * 前台服务
 * Created by chen on 2017/2/8.
 */

public class MoveHintForeService extends Service {
    public static final int MSG_TIME_UPDATE = 0xa1;
    public static final int MSG_DISTANCE_UPDATE = 0xa2;
    private RemoteViews remoteViews;
    private Notification notification;
    private  Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(remoteViews == null){
                return;
            }
            switch (msg.what){
                case MSG_TIME_UPDATE:
                    remoteViews.setTextViewText(R.id.time_notification,msg.obj.toString());
                    notification.contentView = remoteViews;
                    startForeground(1,notification);
                    break;
                case MSG_DISTANCE_UPDATE:
                    remoteViews.setTextViewText(R.id.distance_notification,msg.obj.toString());
                    notification.contentView = remoteViews;
                    startForeground(1,notification);
                    break;
            }
        }
    };

    private final Messenger msssenger = new Messenger(msgHandler);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return msssenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CFLog.e(this.getClass().getName(),"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        remoteViews = new RemoteViews(getPackageName(),R.layout.notification_data);
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.mipmap.app_icon);
        notification = builder.getNotification();
        startForeground(1,notification);
        CFLog.e(this.getClass().getName(),"onCreate");
    }
}

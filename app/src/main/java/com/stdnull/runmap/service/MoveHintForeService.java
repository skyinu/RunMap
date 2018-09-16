package com.stdnull.runmap.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.stdnull.baselib.common.CFLog;
import com.stdnull.runmap.R;

/**
 * 前台服务
 * Created by chen on 2017/2/8.
 */

public class MoveHintForeService extends Service {
    public static final int MSG_TIME_UPDATE = 0xa1;
    public static final int MSG_DISTANCE_UPDATE = 0xa2;
    private RemoteViews remoteViews;
    private Notification notification;
    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (remoteViews == null) {
                return;
            }
            switch (msg.what) {
                case MSG_TIME_UPDATE:
                    remoteViews.setTextViewText(R.id.time_notification, msg.obj.toString());
                    notification.contentView = remoteViews;
                    startForeground(1, notification);
                    break;
                case MSG_DISTANCE_UPDATE:
                    remoteViews.setTextViewText(R.id.distance_notification, msg.obj.toString());
                    notification.contentView = remoteViews;
                    startForeground(1, notification);
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
        CFLog.e(this.getClass().getName(), "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hint_service");
        remoteViews = new RemoteViews(getPackageName(), R.layout.notification_data);
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setChannelId("hint_service");
        notification = builder.build();
        startForeground(1, notification);
        CFLog.e(this.getClass().getName(), "onCreate");
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "hint_service";
        CharSequence channelName = "move hint";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}

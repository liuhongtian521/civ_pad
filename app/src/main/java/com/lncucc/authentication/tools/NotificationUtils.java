package com.lncucc.authentication.tools;

import android.annotation.TargetApi;
        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
        import android.os.Build;
        import android.text.TextUtils;
        import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Created by MoXiao on 2018/4/15.
 */

public class NotificationUtils {
    private static final String TAG = "NotificationUtils";
    private static final String DEFAULT_CHANNEL_ID = "default_id";
    private static final String DEFAULT_CHANNEL_NAME = "default_channel_name";
    private static final int DEFAULT_NOTIFY_ID = 1;
    /**
     * @param context
     * @param title
     * @param content
     * @param smallIcon 显示在状态栏上的小图标，必需
     */
    public static void notification(Context context, String title, String content, int smallIcon, PendingIntent pendingIntent) {
        //无论对于哪个API Level，都需要
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            boolean buildResult = notification_api_26_or_Above(notificationManager, DEFAULT_CHANNEL_ID);
            if (!buildResult) {
                Log.e(TAG, "Fail to build notifiactionChannel");
                return;
            }
        }
        //NotifyID，这个以前也需要
        int notifyID = DEFAULT_NOTIFY_ID;

        //ChannelID, 指明需要哪个channel。这里我们使用默认的。
        String CHANNEL_ID = DEFAULT_CHANNEL_ID;


        NotificationCompat.Builder nb = new NotificationCompat.Builder(context,CHANNEL_ID);
        Notification notification = nb.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(smallIcon)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(notifyID, notification);
    }

    @TargetApi(26)
    private static boolean notification_api_26_or_Above(NotificationManager notificationManager, String channelId) {
        if (notificationManager == null && TextUtils.isEmpty(channelId)) {
            return false;
        }
        //Android O及以上必需的channelID
        String id = channelId;
        //Channel名,有说这个字段对用户可见。但我还没有找到可见的ROM/Android版本。有知道的同学麻烦告知一下
        String name = DEFAULT_CHANNEL_NAME;
        //Channel的描述
        String description = "我就是一个通知的channel";
        //通知的优先级
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = createDefaultChannel(id, name, description, importance);

        //将NotificationChannel对象传入NotificationManager
        notificationManager.createNotificationChannel(channel);
        return true;
    }

    /**
     * 创建默认封装的通知，其他样子依瓢画葫芦就行
     * @param channelId    见上方注释
     * @param channelName  见上方注释
     * @param desc         见上方注释
     * @param imp          见上方注释
     * @return 生成好的默认行为的channel
     */
    @TargetApi(26)
    private static NotificationChannel createDefaultChannel(String channelId, String channelName, String desc, int imp) {

        NotificationChannel channel = new NotificationChannel(channelId, channelName, imp);

        channel.setDescription(desc);
        //是否使用指示灯
        channel.enableLights(true);
        //指示灯颜色，这个取决于设备是否支持
        channel.setLightColor(Color.RED);

        //是否使用震动
        channel.enableVibration(true);
        //震动节奏
        channel.setVibrationPattern(new long[]{100, 200});

        return channel;
    }
}

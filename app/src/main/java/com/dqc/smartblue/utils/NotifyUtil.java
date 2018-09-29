package com.dqc.smartblue.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.core.app.NotificationCompat;

@SuppressLint("NewApi")
public class NotifyUtil {

    private static final int FLAG = Notification.FLAG_INSISTENT;
    int requestCode = (int) SystemClock.uptimeMillis();
    private int                 NOTIFICATION_ID;
    private NotificationManager mNotificationManager;
    private Notification        mNotification;

    private NotificationCompat.Builder mCBuilder;
    private Notification.Builder       mNBuilder;
    private Context                    mContext;


    public NotifyUtil(Context context, int ID) {
        this.NOTIFICATION_ID = ID;
        mContext = context;
        // 获取系统服务来初始化对象
        mNotificationManager = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
        mCBuilder = new NotificationCompat.Builder(mContext);
    }

    /**
     * @param context
     * @param ID
     * @param autoCancel 点击通知栏的notification后，它会自动被取消消失
     */
    public NotifyUtil(Context context, int ID, boolean autoCancel) {
        this.NOTIFICATION_ID = ID;
        mContext = context;
        // 获取系统服务来初始化对象
        mNotificationManager = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
        mCBuilder = new NotificationCompat.Builder(mContext);
        mCBuilder.setAutoCancel(autoCancel);

    }

    /**
     * 设置在顶部通知栏中的各种信息
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    private void setCompatBuilder(PendingIntent pendingIntent, int smallIcon, String ticker,
                                  String title, String content, boolean sound, boolean vibrate, boolean lights) {
//        // 如果当前Activity启动在前台，则不开启新的Activity。
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        // 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
//        // 将Intent封装进PendingIntent中，点击通知的消息后，就会启动对应的程序
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, FLAG);

        mCBuilder.setContentIntent(pendingIntent);// 该通知要启动的Intent
        mCBuilder.setSmallIcon(smallIcon);// 设置顶部状态栏的小图标
        mCBuilder.setTicker(ticker);// 在顶部状态栏中的提示信息

        mCBuilder.setContentTitle(title);// 设置通知中心的标题
        mCBuilder.setContentText(content);// 设置通知中心中的内容
        mCBuilder.setWhen(System.currentTimeMillis());

        // 将Ongoing设为true 那么notification将不能滑动删除
        // notifyBuilder.setOngoing(true);
        /*
         * 从Android4.1开始，可以通过以下方法，设置notification的优先级，
		 * 优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
		 */
        mCBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        /*
         * Notification.DEFAULT_ALL：铃声、闪光、震动均系统默认。
		 * Notification.DEFAULT_SOUND：系统默认铃声。
		 * Notification.DEFAULT_VIBRATE：系统默认震动。
		 * Notification.DEFAULT_LIGHTS：系统默认闪光。
		 * notifyBuilder.setDefaults(Notification.DEFAULT_ALL);
		 */
        int defaults = 0;

        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }

        mCBuilder.setDefaults(defaults);
    }

    /**
     * 设置builder的信息，在用大文本时会用到这个
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    private void setBuilder(PendingIntent pendingIntent, int smallIcon, String ticker, boolean sound, boolean vibrate, boolean lights) {
        mNBuilder = new Notification.Builder(mContext);
        // 如果当前Activity启动在前台，则不开启新的Activity。
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pIntent = PendingIntent.getActivity(mContext,
//                requestCode, intent, FLAG);
        mNBuilder.setContentIntent(pendingIntent);

        mNBuilder.setSmallIcon(smallIcon);


        mNBuilder.setTicker(ticker);
        mNBuilder.setWhen(System.currentTimeMillis());
        mNBuilder.setPriority(Notification.PRIORITY_MAX);

        int defaults = 0;

        if (sound) {
            defaults |= Notification.DEFAULT_SOUND;
        }
        if (vibrate) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        if (lights) {
            defaults |= Notification.DEFAULT_LIGHTS;
        }

        mNBuilder.setDefaults(defaults);
    }

    /**
     * 普通的通知
     * <p/>
     * 1. 侧滑即消失，下拉通知菜单则在通知菜单显示
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void notifyNormalSingline(PendingIntent pendingIntent, int smallIcon,
                                     String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        sent();
    }

    /**
     * 进行多项设置的通知(在小米上似乎不能设置大图标，系统默认大图标为应用图标)
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notifyMailbox(PendingIntent pendingIntent, int smallIcon, int largeIcon, ArrayList<String> messageList,
                              String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);

        // 将Ongoing设为true 那么notification将不能滑动删除
        //mCBuilder.setOngoing(true);

        /**
         // 删除时
         Intent deleteIntent = new Intent(mContext, DeleteService.class);
         int deleteCode = (int) SystemClock.uptimeMillis();
         // 删除时开启一个服务
         PendingIntent deletePendingIntent = PendingIntent.getService(mContext,
         deleteCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         mCBuilder.setDeleteIntent(deletePendingIntent);

         **/

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), largeIcon);
        mCBuilder.setLargeIcon(bitmap);

        mCBuilder.setDefaults(Notification.DEFAULT_ALL);// 设置使用默认的声音
        //mCBuilder.setVibrate(new long[]{0, 100, 200, 300});// 设置自定义的振动
        // builder.setSound(Uri.parse("file:///sdcard/click.mp3"));

        // 设置通知样式为收件箱样式,在通知中心中两指往外拉动，就能出线更多内容，但是很少见
        //mCBuilder.setNumber(messageList.size());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (String msg : messageList) {
            inboxStyle.addLine(msg);
        }
        inboxStyle.setSummaryText("[" + messageList.size() + "条]" + title);
        mCBuilder.setStyle(inboxStyle);
        sent();
    }

    /**
     * 自定义视图的通知
     *
     * @param remoteViews
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     */
    public void notifyCustomview(RemoteViews remoteViews, PendingIntent pendingIntent,
                                 int smallIcon, String ticker, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, null, null, sound, vibrate, lights);

        mNotification = mCBuilder.build();
        mNotification.contentView = remoteViews;
        // 发送该通知
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * 可以容纳多行提示文本的通知信息 (因为在高版本的系统中才支持，所以要进行判断)
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title         标题
     * @param content       内容
     * @param sound         是否有声音
     * @param vibrate       是否震动
     * @param lights        是否有闪光灯
     */
    public void notifyNormailMoreline(PendingIntent pendingIntent, int smallIcon, String ticker,
                                      String title, String content, boolean sound, boolean vibrate, boolean lights) {

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            notifyNormalSingline(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
            Toast.makeText(mContext, "您的手机低于Android 4.1.2，不支持多行通知显示！！", Toast.LENGTH_SHORT).show();
        } else {
            setBuilder(pendingIntent, smallIcon, ticker, sound, vibrate, lights);
            mNBuilder.setContentTitle(title);
            mNBuilder.setContentText(content);
            mNBuilder.setPriority(Notification.PRIORITY_HIGH);
            mNotification = new Notification.BigTextStyle(mNBuilder).bigText(content).build();
            // 发送该通知
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        }
    }

    /**
     * 有进度条的通知，可以设置为模糊进度或者精确进度
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param content
     */
    public void notifyProgress(PendingIntent pendingIntent, int smallIcon,
                               String ticker, String title, String content, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        /*
         * 因为进度条要实时更新通知栏也就说要不断的发送新的提示，所以这里不建议开启通知声音。
		 * 这里是作为范例，给大家讲解下原理。所以发送通知后会听到多次的通知声音。
		 */

        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr += 10) {
                    // 参数：1.最大进度， 2.当前进度， 3.是否有准确的进度显示
                    mCBuilder.setProgress(100, incr, false);
                    // mCBuilder.setProgress(0, 0, true);
                    sent();
                    try {
                        Thread.sleep(1 * 500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 进度满了后，设置提示信息
                mCBuilder.setContentText("下载完成").setProgress(0, 0, false);
                sent();
            }
        }).start();
    }

    /**
     * 容纳大图片的通知
     *
     * @param pendingIntent
     * @param smallIcon
     * @param ticker
     * @param title
     * @param bigPic
     */
    public void notifyBigPic(PendingIntent pendingIntent, int smallIcon, String ticker,
                             String title, String content, int bigPic, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, null, sound, vibrate, lights);
        NotificationCompat.BigPictureStyle picStyle = new NotificationCompat.BigPictureStyle();
        final BitmapFactory.Options        options  = new BitmapFactory.Options();
        options.inScaled = true;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                bigPic, options);
        picStyle.bigPicture(bitmap);
        picStyle.bigLargeIcon(bitmap);
        mCBuilder.setContentText(content);
        mCBuilder.setStyle(picStyle);
        sent();
    }

    /**
     * 里面有两个按钮的通知
     *
     * @param smallIcon
     * @param leftbtnicon
     * @param lefttext
     * @param leftPendIntent
     * @param rightbtnicon
     * @param righttext
     * @param rightPendIntent
     * @param ticker
     * @param title
     * @param content
     */
    public void notifyButton(int smallIcon, int leftbtnicon, String lefttext, PendingIntent leftPendIntent, int rightbtnicon, String righttext, PendingIntent rightPendIntent, String ticker,
                             String title, String content, boolean sound, boolean vibrate, boolean lights) {

        requestCode = (int) SystemClock.uptimeMillis();
        setCompatBuilder(rightPendIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        mCBuilder.addAction(leftbtnicon,
                lefttext, leftPendIntent);
        mCBuilder.addAction(rightbtnicon,
                righttext, rightPendIntent);
        sent();
    }

    public void notifyHeadUp(PendingIntent pendingIntent, int smallIcon, int largeIcon,
                             String ticker, String title, String content, int leftbtnicon, String lefttext, PendingIntent leftPendingIntent, int rightbtnicon, String righttext, PendingIntent rightPendingIntent, boolean sound, boolean vibrate, boolean lights) {

        setCompatBuilder(pendingIntent, smallIcon, ticker, title, content, sound, vibrate, lights);
        mCBuilder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCBuilder.addAction(leftbtnicon,
                    lefttext, leftPendingIntent);
            mCBuilder.addAction(rightbtnicon,
                    righttext, rightPendingIntent);
        } else {
            Toast.makeText(mContext, "版本低于Andriod5.0，无法体验HeadUp样式通知", Toast.LENGTH_SHORT).show();
        }
        sent();
    }


    /**
     * 发送通知
     */
    private void sent() {
        mNotification = mCBuilder.build();
        // 发送该通知
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);
    }

    /**
     * 根据id清除通知
     */
    public void clear() {
        // 取消通知
        mNotificationManager.cancelAll();
    }
}

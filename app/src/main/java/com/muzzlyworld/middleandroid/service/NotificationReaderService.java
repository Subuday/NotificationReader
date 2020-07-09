package com.muzzlyworld.middleandroid.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationCompat;

import com.muzzlyworld.middleandroid.App;
import com.muzzlyworld.middleandroid.R;
import com.muzzlyworld.middleandroid.core.NotificationRepository;

import java.util.UUID;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class NotificationReaderService extends NotificationListenerService {

    public static final String IS_ACTIVE = "isActive";

    private NotificationRepository mNotificationRepository;

    private boolean mIsActive = false;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationRepository = ((App) getApplication()).appContainer.notificationRepository;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final boolean isActive = intent.getBooleanExtra(IS_ACTIVE, false);
        if (mIsActive != isActive) {
            mIsActive = isActive;
            if (mIsActive) startServiceInForeground();
            else stopService();
        }
        return Service.START_STICKY;
    }

    public void startServiceInForeground() {
        createNotificationChannel();
        Notification notification = new NotificationCompat.Builder(this, "NotificationReaderChannelId")
                .setContentTitle("Notification Reader")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        startForeground(12321, notification);
        mNotificationRepository.setIsReadingNotifications(true);
    }

    private void stopService() {
        mNotificationRepository.setIsReadingNotifications(false);
        mCompositeDisposable.clear();
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        stopService();
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (shouldHandleNotification(sbn)) {
            final String id = UUID.randomUUID().toString();
            final String packageName = sbn.getPackageName();
            final String appName = getAppNameFromPackage(packageName);
            final String data = getData(sbn.getNotification());
            final Long postTime = sbn.getPostTime();
            final com.muzzlyworld.middleandroid.core.Notification notification
                    = new com.muzzlyworld.middleandroid.core.Notification(id, packageName, appName, data, postTime);
            Disposable disposable = mNotificationRepository.insertNotification(notification)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
            mCompositeDisposable.add(disposable);
        }
    }

    private boolean shouldHandleNotification(StatusBarNotification sbn) {
        return mIsActive;
    }

    private String getAppNameFromPackage(String pkg) {
        final PackageManager packageManager = getApplicationContext().getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(pkg, 0);
        } catch (final PackageManager.NameNotFoundException exception) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "unknown");
    }

    private String getData(Notification notification) {
        return notification.extras.getCharSequence(Notification.EXTRA_TEXT, "").toString();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "NotificationReaderChannelId",
                    "Notification Reader",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}


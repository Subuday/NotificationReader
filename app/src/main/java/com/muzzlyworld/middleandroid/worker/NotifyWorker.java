package com.muzzlyworld.middleandroid.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.muzzlyworld.middleandroid.R;
import com.muzzlyworld.middleandroid.core.NotificationRepository;

public class NotifyWorker extends Worker {

    public static final String TAG  = NotifyWorker.class.getName();

    private final Context mContext;
    private final NotificationRepository mNotificationRepository;

    public NotifyWorker(@NonNull Context context,
                        @NonNull WorkerParameters params,
                        @NonNull NotificationRepository repository) {
        super(context, params);
        mContext = context;
        mNotificationRepository = repository;
    }

    @NonNull
    @Override
    public Result doWork() {
        int numberOfNewNotifications = mNotificationRepository.getNumberOfNewNotifications();
        createNotification(numberOfNewNotifications);
        return Result.success();
    }

    private void createNotification(int numberOfNewNotifications) {
        Notification notification = new NotificationCompat.Builder(mContext, "NotificationReaderChannelId")
                .setContentTitle("Notification Reader")
                .setContentText("New Notifications: " + numberOfNewNotifications)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    "NotifierId",
                    "Notifier",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(123242, notification);
    }
}

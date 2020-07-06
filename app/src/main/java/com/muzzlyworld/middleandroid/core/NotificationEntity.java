package com.muzzlyworld.middleandroid.core;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public final class NotificationEntity {
    @PrimaryKey
    @NonNull public String id;
    @NonNull public String packageName;
    @NonNull public String appName;
    @NonNull public String data;
    @NonNull public Long postTime;

    public Notification toNotification() {
        return new Notification(id, packageName, appName, data, postTime);
    }

    public static NotificationEntity toNotificationEntity(Notification notification) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.id = notification.getId();
        notificationEntity.packageName = notification.getPackageName();
        notificationEntity.appName = notification.getAppName();
        notificationEntity.data = notification.getData();
        notificationEntity.postTime = notification.getPostTime();
        return notificationEntity;
    }
}

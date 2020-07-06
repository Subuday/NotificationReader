package com.muzzlyworld.middleandroid.core;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface NotificationRepository {
    Single<List<Notification>> getNotifications(DateFilter dateFilter);
    Flowable<Notification> observeNewNotifications();
    int getNumberOfNewNotifications();
    Completable insertNotification(Notification notification);
    Flowable<Boolean> observeIsReadingNotifications();
    void setIsReadingNotifications(boolean isReadingNotifications);
}

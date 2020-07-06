package com.muzzlyworld.middleandroid.core;

import com.muzzlyworld.middleandroid.utils.TimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationDao dao;

    private final PublishSubject<Notification> mNewNotifications = PublishSubject.create();
    private final AtomicInteger mNumberOfNewNotification = new AtomicInteger(0);

    private final BehaviorSubject<Boolean> mIsReadingNotifications = BehaviorSubject.createDefault(false);

    public NotificationRepositoryImpl(NotificationDao dao) {
        this.dao = dao;
    }

    @Override
    public Single<List<Notification>> getNotifications(DateFilter dateFilter) {
        long timeLimit;
        long currentTime =  Calendar.getInstance().getTimeInMillis();
        switch (dateFilter) {
            case PER_HOUR: timeLimit = currentTime - TimeUtils.getHoursInMillis(1); break;
            case PER_DAY: timeLimit = currentTime - TimeUtils.getHoursInMillis(24); break;
            case PER_MONTH: timeLimit = currentTime - TimeUtils.getDayInMillis(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)); break;
            default: timeLimit = 0L;
        }
        return dao.getNotifications(timeLimit)
                .flatMapObservable(Observable::fromIterable)
                .map(NotificationEntity::toNotification)
                .toList();
    }

    @Override
    public int getNumberOfNewNotifications() {
        return mNumberOfNewNotification.get();
    }

    @Override
    public Flowable<Notification> observeNewNotifications() {
        return mNewNotifications.toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable insertNotification(Notification notification) {
        mNumberOfNewNotification.incrementAndGet();
        mNewNotifications.onNext(notification);
        return dao.insert(NotificationEntity.toNotificationEntity(notification));
    }

    @Override
    public Flowable<Boolean> observeIsReadingNotifications() {
        return mIsReadingNotifications.toFlowable(BackpressureStrategy.LATEST);
    }

    @Override
    public void setIsReadingNotifications(boolean isReadingNotifications) {
        if(!isReadingNotifications) clearNumberOfNewNotifications();
        mIsReadingNotifications.onNext(isReadingNotifications);
    }

    private void clearNumberOfNewNotifications() {
        mNumberOfNewNotification.set(0);
    }
}

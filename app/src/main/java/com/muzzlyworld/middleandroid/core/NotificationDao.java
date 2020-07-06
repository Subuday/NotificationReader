package com.muzzlyworld.middleandroid.core;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notifications WHERE postTime >= :timeLimit ORDER BY postTime DESC")
    Single<List<NotificationEntity>> getNotifications(Long timeLimit);

    @Insert
    Completable insert(NotificationEntity notification);

}

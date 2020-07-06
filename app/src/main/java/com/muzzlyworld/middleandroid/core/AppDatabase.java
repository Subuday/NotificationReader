package com.muzzlyworld.middleandroid.core;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {NotificationEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotificationDao notificationDao();
}

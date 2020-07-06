package com.muzzlyworld.middleandroid.di;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.muzzlyworld.middleandroid.core.AppDatabase;
import com.muzzlyworld.middleandroid.core.NotificationRepository;
import com.muzzlyworld.middleandroid.core.NotificationRepositoryImpl;

//For simplicity don't use Singleton
public final class AppContainer {

    private static volatile AppContainer sInstance;

    private Context applicationContent;
    private AppDatabase database;
    public NotificationRepository notificationRepository;

    private AppContainer(@NonNull Context context) {
        applicationContent = context;
        database = Room.databaseBuilder(applicationContent, AppDatabase.class, "appdatabase").build();
        notificationRepository = new NotificationRepositoryImpl(database.notificationDao());
    }

    public static AppContainer getInstance(@NonNull Context applicationContext) {
        if(sInstance == null) {
            synchronized (AppContainer.class) {
                if(sInstance == null) { sInstance = new AppContainer(applicationContext); }
            }
        }
        return sInstance;
    }
}

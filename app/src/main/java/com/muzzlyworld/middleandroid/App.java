package com.muzzlyworld.middleandroid;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.Configuration;

import com.muzzlyworld.middleandroid.di.AppContainer;
import com.muzzlyworld.middleandroid.worker.NotificationWorkerFactory;

public class App extends Application implements Configuration.Provider {

    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        appContainer = AppContainer.getInstance(getApplicationContext());
    }

    @NonNull
    @Override
    public Configuration getWorkManagerConfiguration() {
        NotificationWorkerFactory notificationWorkerFactory = new NotificationWorkerFactory(appContainer.notificationRepository);
        return new Configuration.Builder().setWorkerFactory(notificationWorkerFactory).build();
    }
}

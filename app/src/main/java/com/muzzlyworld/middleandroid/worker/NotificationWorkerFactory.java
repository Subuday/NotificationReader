package com.muzzlyworld.middleandroid.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.ListenableWorker;
import androidx.work.WorkerFactory;
import androidx.work.WorkerParameters;

import com.muzzlyworld.middleandroid.core.NotificationRepository;

public final class NotificationWorkerFactory extends WorkerFactory {

    private NotificationRepository mNotificationRepository;

    public NotificationWorkerFactory(NotificationRepository notificationRepository) {
        mNotificationRepository = notificationRepository;
    }

    @Nullable
    @Override
    public ListenableWorker createWorker(@NonNull Context appContext, @NonNull String workerClassName, @NonNull WorkerParameters workerParameters) {
        return new NotifyWorker(appContext, workerParameters, mNotificationRepository);
    }
}

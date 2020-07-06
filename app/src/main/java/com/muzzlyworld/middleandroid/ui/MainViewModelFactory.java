package com.muzzlyworld.middleandroid.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.muzzlyworld.middleandroid.core.NotificationRepository;

public final class MainViewModelFactory implements ViewModelProvider.Factory {

    private NotificationRepository mNotificationRepository;
    private Context mContext;


    public MainViewModelFactory(@NonNull Context applicationContext, @NonNull NotificationRepository notificationRepository) {
        this.mContext = applicationContext;
        this.mNotificationRepository = notificationRepository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(mContext, mNotificationRepository);
    }
}

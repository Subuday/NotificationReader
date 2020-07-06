package com.muzzlyworld.middleandroid.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.muzzlyworld.middleandroid.core.DateFilter;
import com.muzzlyworld.middleandroid.core.Notification;
import com.muzzlyworld.middleandroid.core.NotificationRepository;
import com.muzzlyworld.middleandroid.service.NotificationReaderService;
import com.muzzlyworld.middleandroid.utils.Event;
import com.muzzlyworld.middleandroid.worker.NotifyWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class MainViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    private final Context mApplicationContext;

    private final NotificationRepository mNotificationRepository;

    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private final MutableLiveData<ViewState> mState = new MutableLiveData<>(new ViewState.Builder().build());
    private final MutableLiveData<Event<Effect>> mEffect = new MutableLiveData<>();

    public MainViewModel(Context applicationContext, NotificationRepository notificationRepository) {
        this.mApplicationContext = applicationContext;
        this.mNotificationRepository = notificationRepository;
        loadInitialNotifications();
        observeNewNotifications();
        observeIsReadingNotifications();
    }

    private void loadInitialNotifications() {
        Disposable disposable = mNotificationRepository.getNotifications(mState.getValue().getDateFilter())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notifications -> {
                    ViewState newState = mState.getValue().builder()
                            .notifications(notifications)
                            .showNoNotifications(notifications.isEmpty())
                            .build();
                    setState(newState);
                });
        mCompositeDisposable.add(disposable);
    }

    private void observeNewNotifications() {
        Disposable disposable = mNotificationRepository.observeNewNotifications()
                .subscribe(notification -> {
                    ViewState oldState = mState.getValue();
                    List<Notification> notifications = new ArrayList<>();
                    notifications.add(notification);
                    notifications.addAll(oldState.getNotifications());
                    ViewState newState = oldState.builder()
                            .notifications(notifications)
                            .showNoNotifications(false)
                            .build();
                    setState(newState);
                });
        mCompositeDisposable.add(disposable);
    }

    private void observeIsReadingNotifications() {
        Disposable disposable = mNotificationRepository.observeIsReadingNotifications()
                .subscribe(isReadingNotification -> {
                    if (isReadingNotification) setNotificationListeningActive();
                    else setNotificationListeningInactive();
                });
        mCompositeDisposable.add(disposable);
    }

    public void onFilterAllClick() {
        ViewState newState = mState.getValue().builder()
                .dateFilter(DateFilter.ALL)
                .build();
        setState(newState);
        loadInitialNotifications();
    }

    public void onFilterPerHourClick() {
        ViewState newState = mState.getValue().builder()
                .dateFilter(DateFilter.PER_HOUR)
                .build();
        setState(newState);
        loadInitialNotifications();
    }

    public void onFilterPerDayClick() {
        ViewState newState = mState.getValue().builder()
                .dateFilter(DateFilter.PER_DAY)
                .build();
        setState(newState);
        loadInitialNotifications();
    }

    public void onFilterPerMonthClick() {
        ViewState newState = mState.getValue().builder()
                .dateFilter(DateFilter.PER_MONTH)
                .build();
        setState(newState);
        loadInitialNotifications();
    }

    public void onStartClick() {
        if (mState.getValue().isActive()) stopNotificationListening();
        else startNotificationListening();
    }

    public void setNotificationListeningActive() {
        final ViewState newState = mState.getValue().builder()
                .isActive(true)
                .build();
        WorkRequest workRequest =
                new PeriodicWorkRequest
                        .Builder(NotifyWorker.class, 3, TimeUnit.HOURS)
                        .setInitialDelay(5, TimeUnit.MINUTES)
                        .addTag(NotifyWorker.TAG)
                        .build();
        WorkManager.getInstance(mApplicationContext).enqueue(workRequest);
        setState(newState);
    }

    public void setNotificationListeningInactive() {
        final ViewState newState = mState.getValue().builder()
                .isActive(false)
                .build();
        WorkManager.getInstance(mApplicationContext).cancelAllWorkByTag(NotifyWorker.TAG);
        setState(newState);
    }

    private void startNotificationListening() {
        if (!NotificationManagerCompat.getEnabledListenerPackages(mApplicationContext).contains(mApplicationContext.getPackageName())) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplicationContext.startActivity(intent);
        } else {
            Intent intent = new Intent(mApplicationContext, NotificationReaderService.class);
            intent.putExtra(NotificationReaderService.IS_ACTIVE, true);
            mApplicationContext.startService(intent);
        }
    }

    private void stopNotificationListening() {
        Intent intent = new Intent(mApplicationContext, NotificationReaderService.class);
        intent.putExtra(NotificationReaderService.IS_ACTIVE, false);
        mApplicationContext.startService(intent);
    }

    public void onFilterClick() {
        effect(new Effect.ShowFilterDialog());
    }

    private void setState(ViewState state) {
        mState.setValue(state);
    }

    private void effect(Effect effect) {
        mEffect.setValue(new Event<>(effect));
    }

    public LiveData<ViewState> getState() {
        return mState;
    }

    public LiveData<Event<Effect>> getEffect() {
        return mEffect;
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
    }
}



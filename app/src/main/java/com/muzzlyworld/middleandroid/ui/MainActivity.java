package com.muzzlyworld.middleandroid.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.muzzlyworld.middleandroid.App;
import com.muzzlyworld.middleandroid.R;
import com.muzzlyworld.middleandroid.databinding.ActivityMainBinding;
import com.muzzlyworld.middleandroid.utils.ThemeUtils;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private MainViewModel mViewModel;

    private NotificationAdapter mAdapter;

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
       if(isGranted) mViewModel.onStartClick();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(getApplicationContext(), ((App) getApplication()).appContainer.notificationRepository);
        mViewModel = new ViewModelProvider(this, mainViewModelFactory).get(MainViewModel.class);

        setup();

        mViewModel.getState().observe(this, this::render);
        mViewModel.getEffect().observe(this, effectEvent -> {
            Effect effect = effectEvent.getContentIfNotHandled();
            if(effect != null) handleEffect(effect);
        });
    }

    private void render(ViewState viewState) {
        if (viewState.isShowNoNotifications()) mBinding.noNotifications.setVisibility(View.VISIBLE);
        else mBinding.noNotifications.setVisibility(View.GONE);
        mAdapter.submitList(viewState.getNotifications());
        if(viewState.isActive()) {
            mBinding.start.setText("Stop");
            mBinding.start.setBackgroundColor(getResources().getColor(R.color.colorYellow));
            mBinding.start.setTextColor(ThemeUtils.getThemeColor(this, R.attr.colorAccent));
        } else {
            mBinding.start.setText("Start");
            mBinding.start.setBackgroundColor(ThemeUtils.getThemeColor(this, R.attr.colorAccent));
            mBinding.start.setTextColor(ThemeUtils.getThemeColor(this, R.attr.colorPrimary));
        }
    }

    private void handleEffect(Effect effect) {
        if(effect instanceof Effect.ShowFilterDialog) { showFilterDialog(); }
    }

    private void setup() {
        mAdapter = new NotificationAdapter();
        mBinding.notifications.setAdapter(mAdapter);
        mBinding.filter.setOnClickListener(view -> mViewModel.onFilterClick());
        mBinding.start.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) checkPermissionForForegroundService();
            else mViewModel.onStartClick();
        });
    }

    private void showFilterDialog() {
        new FilterDialog().show(getSupportFragmentManager(), FilterDialog.class.getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void checkPermissionForForegroundService() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
            mViewModel.onStartClick();
        } else requestPermissionLauncher.launch(Manifest.permission.FOREGROUND_SERVICE);
    }
}
package com.muzzlyworld.middleandroid.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.muzzlyworld.middleandroid.R;
import com.muzzlyworld.middleandroid.databinding.DialogFilterBinding;

public final class FilterDialog extends DialogFragment {

    private DialogFilterBinding mBinging;

    private MainViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinging = DialogFilterBinding.inflate(inflater, container, false);
        return mBinging.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = requireDialog().getWindow();
        if (window == null) dismiss();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.dialog_filter_with);
        params.height = getResources().getDimensionPixelSize(R.dimen.dialog_filter_height);
        params.gravity = Gravity.TOP | Gravity.END;
        params.y = getResources().getDimensionPixelSize(R.dimen.dialog_filter_margin_top);
        params.x = getResources().getDimensionPixelSize(R.dimen.dialog_filter_margin_start);
        window.setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        ViewState state = mViewModel.getState().getValue();
        switch (state.getDateFilter()) {
            case ALL:
                mBinging.all.setChecked(true);
                break;
            case PER_HOUR:
                mBinging.perHour.setChecked(true);
                break;
            case PER_DAY:
                mBinging.perDay.setChecked(true);
                break;
            case PER_MONTH:
                mBinging.perMonth.setChecked(true);
                break;
        }
        mBinging.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(mBinging.all.getId() == checkedId) { mViewModel.onFilterAllClick(); }
            else if(mBinging.perHour.getId() == checkedId) { mViewModel.onFilterPerHourClick(); }
            else if(mBinging.perDay.getId() == checkedId) { mViewModel.onFilterPerDayClick(); }
            else if(mBinging.perMonth.getId() == checkedId) { mViewModel.onFilterPerMonthClick(); }
        });
    }
}

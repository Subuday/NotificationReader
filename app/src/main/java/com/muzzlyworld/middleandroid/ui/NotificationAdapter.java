package com.muzzlyworld.middleandroid.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.muzzlyworld.middleandroid.core.Notification;
import com.muzzlyworld.middleandroid.databinding.ItemNotificationBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class NotificationAdapter extends ListAdapter<Notification, NotificationAdapter.NotificationViewHolder> {

    public NotificationAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return NotificationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final ItemNotificationBinding mBinding;

        private NotificationViewHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(Notification notification) {
            mBinding.appName.setText(notification.getAppName());
            mBinding.data.setText(notification.getData());

            final Date date = new Date(notification.getPostTime());
            final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
            mBinding.date.setText(dateFormat.format(date));

            final Date time = new Date(notification.getPostTime());
            final DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            mBinding.time.setText(timeFormat.format(time));

            try {
                final Context context = mBinding.getRoot().getContext();
                mBinding.appImage.setImageDrawable(context.getPackageManager().getApplicationIcon(notification.getPackageName()));
            } catch (PackageManager.NameNotFoundException exception) { }
        }

        public static NotificationViewHolder create(ViewGroup parent) {
            ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new NotificationViewHolder(binding);
        }
    }

    public static final DiffUtil.ItemCallback<Notification> DIFF_CALLBACK = new DiffUtil.ItemCallback<Notification>() {
        @Override
        public boolean areItemsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Notification oldItem, @NonNull Notification newItem) {
            return oldItem.equals(newItem);
        }
    };

}

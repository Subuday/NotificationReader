package com.muzzlyworld.middleandroid.ui;

import com.muzzlyworld.middleandroid.core.DateFilter;
import com.muzzlyworld.middleandroid.core.Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ViewState {

    private final boolean isActive;
    private final List<Notification> notifications;
    private final boolean showNoNotifications;
    private final DateFilter dateFilter;

    private ViewState(boolean isActive,
                      List<Notification> notifications,
                      boolean showNoNotifications,
                      DateFilter dateFilter) {
        this.isActive = isActive;
        this.notifications = notifications;
        this.showNoNotifications = showNoNotifications;
        this.dateFilter = dateFilter;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public boolean isShowNoNotifications() {
        return showNoNotifications;
    }

    public DateFilter getDateFilter() {
        return dateFilter;
    }

    public Builder builder() {
        return new Builder(this);
    }

    public static final class Builder {
        private boolean isActive;
        private List<Notification> notifications;
        private boolean showNoNotifications;
        private DateFilter dateFilter;

        public Builder() {
            isActive = false;
            notifications = Collections.emptyList();
            showNoNotifications = false;
            dateFilter = DateFilter.ALL;
        }

        public Builder(ViewState toCopyFrom) {
            this.isActive = toCopyFrom.isActive;
            this.notifications = new ArrayList<>(toCopyFrom.notifications.size());
            this.notifications.addAll(toCopyFrom.notifications);
            this.showNoNotifications = toCopyFrom.showNoNotifications;
            this.dateFilter = toCopyFrom.dateFilter;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder notifications(List<Notification> notifications) {
            this.notifications = notifications;
            return this;
        }

        public Builder showNoNotifications(boolean showNoNotifications) {
            this.showNoNotifications = showNoNotifications;
            return this;
        }

        public Builder dateFilter(DateFilter dateFilter) {
            this.dateFilter = dateFilter;
            return this;
        }

        public ViewState build() {
            return new ViewState(isActive, notifications, showNoNotifications, dateFilter);
        }
    }
}

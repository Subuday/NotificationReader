package com.muzzlyworld.middleandroid.core;

import androidx.annotation.NonNull;

import java.util.Objects;

public final class Notification {
    private final String id;
    private final String packageName;
    private final String appName;
    private final String data;
    private final Long postTime;

    public Notification(@NonNull String id,
                        @NonNull String packageName,
                        @NonNull String appName,
                        @NonNull String data,
                        @NonNull Long postTime) {
        this.id = id;
        this.packageName = packageName;
        this.appName = appName;
        this.data = data;
        this.postTime = postTime;
    }

    public String getId() {
        return id;
    }

    public String getPackageName() { return packageName; }

    public String getAppName() {
        return appName;
    }

    public Long getPostTime() { return postTime; }

    public String getData() { return data; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id.equals(that.id) &&
                packageName.equals(that.packageName) &&
                appName.equals(that.appName) &&
                data.equals(that.data) &&
                postTime.equals(that.postTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, packageName, appName, data, postTime);
    }
}

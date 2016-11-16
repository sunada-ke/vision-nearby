package com.lakeel.profile.notification.presentation.view;

import com.firebase.geofire.GeoLocation;

import android.support.annotation.StringRes;

public interface TrackingView {

    void showLocationMap(GeoLocation location);

    void showSnackBar(@StringRes int resId);

    void showDetectedDate(String detectedTime);
}

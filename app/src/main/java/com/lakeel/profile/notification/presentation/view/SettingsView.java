package com.lakeel.profile.notification.presentation.view;

import android.support.annotation.StringRes;

public interface SettingsView extends BaseView {

    void showCMPreferences();

    void showSnackBar(@StringRes int resId);
}

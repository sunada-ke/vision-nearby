package com.lakeel.altla.vision.nearby.presentation.view;


import android.content.Intent;
import android.support.annotation.StringRes;

public interface SignInView extends BaseView {

    void showSignInActivity(Intent intent);

    void onSignedIn();

    void showSnackBar(@StringRes int resId);
}
package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;

import android.support.annotation.StringRes;

import java.util.ArrayList;

public interface FavoriteView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

    void showProfile(UserModel model);

    void showShareSheet();

    void initializeOptionMenu();

    void showLineUrl(String url);

    void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName);
}

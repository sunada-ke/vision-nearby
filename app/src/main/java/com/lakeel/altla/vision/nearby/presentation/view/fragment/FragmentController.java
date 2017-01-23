package com.lakeel.altla.vision.nearby.presentation.view.fragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bluetooth.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.distance.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteUserFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.user.UserPassingFragment;

import java.util.ArrayList;

public final class FragmentController {

    private final String SIGN_IN_FRAGMENT_TAG = SignInFragment.class.getSimpleName();

    private final String FAVORITE_LIST_FRAGMENT_TAG = FavoriteListFragment.class.getSimpleName();

    private final String HISTORY_FRAGMENT_TAG = HistoryListFragment.class.getSimpleName();

    private final String USER_PASSING_FRAGMENT_TAG = UserPassingFragment.class.getSimpleName();

    private final String NEARBY_LIST_FRAGMENT = NearbyListFragment.class.getSimpleName();

    private final String TRACKING_FRAGMENT_TAG = TrackingFragment.class.getSimpleName();

    private final String DEVICE_LIST_FRAGMENT_TAG = DeviceListFragment.class.getSimpleName();

    private final String DISTANCE_ESTIMATION_FRAGMENT_TAG = DistanceEstimationFragment.class.getSimpleName();

    private final String USER_PROFILE_FRAGMENT_TAG = FavoriteUserFragment.class.getSimpleName();

    private final String INFORMATION_LIST_FRAGMENT_TAG = InformationListFragment.class.getSimpleName();

    private final String INFORMATION_FRAGMENT_TAG = InformationFragment.class.getSimpleName();

    private final String SETTINGS_FRAGMENT_TAG = SettingsFragment.class.getSimpleName();

    private final String SETTINGS_BLE_FRAGMENT_TAG = BleSettingsFragment.class.getSimpleName();

    private final String SETTINGS_LINE_FRAGMENT_TAG = LineSettingsFragment.class.getSimpleName();

    private FragmentManager fragmentManager;

    public FragmentController(FragmentActivity activity) {
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public FragmentController(Fragment fragment) {
        this.fragmentManager = fragment.getActivity().getSupportFragmentManager();
    }

    public void showSignInFragment() {
        SignInFragment fragment = SignInFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SIGN_IN_FRAGMENT_TAG);
    }

    public void showFavoriteListFragment() {
        FavoriteListFragment fragment = FavoriteListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, FAVORITE_LIST_FRAGMENT_TAG);
    }

    public void showNearbyListFragment() {
        NearbyListFragment fragment = NearbyListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, NEARBY_LIST_FRAGMENT);
    }

    public void showHistoryListFragment() {
        HistoryListFragment fragment = HistoryListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, HISTORY_FRAGMENT_TAG);
    }

    public void showUserPassingFragment(String historyId) {
        UserPassingFragment fragment = UserPassingFragment.newInstance(historyId);
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_PASSING_FRAGMENT_TAG);
    }

    public void showTrackingFragment(String id, String beaconName) {
        TrackingFragment fragment = TrackingFragment.newInstance(id, beaconName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, TRACKING_FRAGMENT_TAG);
    }

    public void showDeviceListFragment() {
        DeviceListFragment fragment = DeviceListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, DEVICE_LIST_FRAGMENT_TAG);
    }

    public void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName) {
        DistanceEstimationFragment fragment = DistanceEstimationFragment.newInstance(beaconIds, targetName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, DISTANCE_ESTIMATION_FRAGMENT_TAG);
    }

    public void showFavoriteUserFragment(String userId, String userName) {
        FavoriteUserFragment fragment = FavoriteUserFragment.newInstance(userId, userName);
        replaceFragment(R.id.fragmentPlaceholder, fragment, USER_PROFILE_FRAGMENT_TAG);
    }

    public void showBleSettingsFragment() {
        BleSettingsFragment fragment = BleSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_BLE_FRAGMENT_TAG);
    }

    public void showLineSettingsFragment() {
        LineSettingsFragment fragment = LineSettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_LINE_FRAGMENT_TAG);
    }

    public void showInformationListFragment() {
        InformationListFragment fragment = InformationListFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, INFORMATION_LIST_FRAGMENT_TAG);
    }

    public void showInformationFragment(String informationId) {
        InformationFragment fragment = InformationFragment.newInstance(informationId);
        replaceFragment(R.id.fragmentPlaceholder, fragment, INFORMATION_FRAGMENT_TAG);
    }

    public void showSettingsFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        replaceFragment(R.id.fragmentPlaceholder, fragment, SETTINGS_FRAGMENT_TAG);
    }

    private void replaceFragment(@IdRes int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
}

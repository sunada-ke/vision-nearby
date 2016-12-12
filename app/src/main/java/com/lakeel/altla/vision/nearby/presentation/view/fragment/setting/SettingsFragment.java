package com.lakeel.altla.vision.nearby.presentation.view.fragment.setting;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.setting.SettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.transaction.FragmentController;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

import javax.inject.Inject;

public final class SettingsFragment extends PreferenceFragmentCompat implements SettingsView {

    private static final String KEY_SNS_CATEGORY = "snsCategory";

    private static final String KEY_BLUETOOTH_SCREEN = "bluetoothScreen";

    private static final String KEY_LINE_SCREEN = "lineScreen";

    private static final String KEY_CM_SCREEN = "cmScreen";

    private static final String KEY_TRACKING_SCREEN = "trackingScreen";

    private PreferenceCategory snsCategory;

    private PreferenceScreen cmScreen;

    @Inject
    SettingsPresenter presenter;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_fragment);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        // BLE
        PreferenceScreen bleScreen = (PreferenceScreen) findPreference(KEY_BLUETOOTH_SCREEN);
        bleScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
            controller.showBleSettingsFragment();
            return false;
        });

        // LINE
        PreferenceScreen lineScreen = (PreferenceScreen) findPreference(KEY_LINE_SCREEN);
        lineScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
            controller.showLineSettingsFragment();
            return false;
        });

        // COMPANY Messenger
        cmScreen = (PreferenceScreen) findPreference(KEY_CM_SCREEN);
        cmScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
            controller.showCmSettingsFragment();
            return false;
        });

        // Once, hide the menu of COMPANY Messenger.
        snsCategory = (PreferenceCategory) findPreference(KEY_SNS_CATEGORY);
        snsCategory.removePreference(cmScreen);

        // Tracking
        PreferenceScreen trackingScreen = (PreferenceScreen) findPreference(KEY_TRACKING_SCREEN);
        trackingScreen.setOnPreferenceClickListener(preference -> {
            FragmentController controller = new FragmentController(getActivity().getSupportFragmentManager());
            controller.showDeviceListFragment();
            return false;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(true);

        MainActivity activity = (MainActivity) getActivity();
        activity.setDrawerIndicatorEnabled(true);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void showCmPreferences() {
        snsCategory.addPreference(cmScreen);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}

package com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.line;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.MenuItem;
import android.view.View;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.presenter.settings.line.LineSettingsPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingsView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

public final class LineSettingsFragment extends PreferenceFragmentCompat implements LineSettingsView {

    @Inject
    LineSettingsPresenter presenter;

    private static final String KEY_LINE_URL = "lineUrl";

    private EditTextPreference lineUrlPreference;

    public static LineSettingsFragment newInstance() {
        return new LineSettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_line);

        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.title_line_settings);

        ((MainActivity) getActivity()).setDrawerIndicatorEnabled(false);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        lineUrlPreference = (EditTextPreference) findPreference(KEY_LINE_URL);
        lineUrlPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String lineUrl = (String) newValue;
            presenter.onSaveLineUrl(lineUrl);
            return false;
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showLineUrl(String url) {
        lineUrlPreference.setText(url);
        lineUrlPreference.setSummary(url);
    }

    @Override
    public void showSnackBar(@StringRes int resId) {
        View view = getView();
        if (view != null) {
            Snackbar.make(getView(), resId, Snackbar.LENGTH_SHORT).show();
        }
    }
}

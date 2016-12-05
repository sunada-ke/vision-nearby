package com.lakeel.altla.vision.nearby.presentation.view.fragment.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.constants.BundleKey;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.profile.ProfilePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.ProfileView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ProfileFragment extends Fragment implements ProfileView {

    public static ProfileFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.getString(BundleKey.USER_ID.getValue(), userId);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ProfilePresenter presenter;

    @BindView(R.id.nameText)
    public TextView textViewName;

    @BindView(R.id.emailText)
    public TextView textViewEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);

        MainActivity.getUserComponent(this).inject(this);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        String userId = bundle.getString(BundleKey.USER_ID.getValue());
        presenter.setUserId(userId);

        presenter.onActivityCreated();
    }

    @Override
    public void showProfile(UserModel model) {
        textViewName.setText(model.name);
        textViewEmail.setText(model.email);
    }
}

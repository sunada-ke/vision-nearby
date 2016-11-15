package com.lakeel.profile.notification.presentation.presenter.settings;

import com.lakeel.profile.notification.domain.usecase.FindConfigsUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.view.SettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SettingsPresenter extends BasePresenter<SettingsView> {

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsPresenter.class);


    @Inject
    SettingsPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = mFindConfigsUseCase
                .execute()
                .toObservable()
                .filter(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showCMPreferences();
                }, e -> {
                    LOGGER.error("Failed to find CM links settings.", e);
                });

        mCompositeSubscription.add(subscription);
    }
}

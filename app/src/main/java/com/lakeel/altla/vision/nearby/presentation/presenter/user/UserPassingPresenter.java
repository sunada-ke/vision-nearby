package com.lakeel.altla.vision.nearby.presentation.presenter.user;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.UserPassingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class UserPassingPresenter extends BasePresenter<UserPassingView> {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindTimesUseCase findTimesUseCase;

    @Inject
    SaveFavoriteUseCase saveFavoriteUseCase;

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindFavoriteUseCase findFavoriteUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserPassingPresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private String otherUserId;

    private String latitude;

    private String longitude;

    @Inject
    UserPassingPresenter() {
    }

    public void setUserLocationData(String otherUserId, String latitude, String longitude) {
        this.otherUserId = otherUserId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void onActivityCreated() {
        Subscription presenceSubscription = findPresenceUseCase
                .execute(otherUserId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        subscriptions.add(presenceSubscription);

        Subscription timesSubscription = findTimesUseCase.execute(MyUser.getUid(), otherUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times),
                        e -> LOGGER.error("Failed to find times.", e));
        subscriptions.add(timesSubscription);

        Subscription userSubscription = findUserUseCase.execute(otherUserId)
                .map(entity -> userModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showProfile(model),
                        e -> LOGGER.error("Failed to find user.", e));
        subscriptions.add(userSubscription);

        Subscription lineLinkSubscription = findLineLinkUseCase
                .execute(otherUserId)
                .map(lineLinksEntity -> lineLinksEntity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to find LINE link.", e));
        subscriptions.add(lineLinkSubscription);

        Subscription favoriteSubscription = findFavoriteUseCase
                .execute(MyUser.getUid(), otherUserId)
                .toObservable()
                .filter(entity -> entity == null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> getView().showAddButton(),
                        e -> LOGGER.error("Failed to find the user data.", e));
        subscriptions.add(favoriteSubscription);
    }

    public void onMapReady() {
        if (latitude == null && longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocationMap(latitude, longitude);
        }
    }

    public void onAdd() {
        Subscription subscription = saveFavoriteUseCase
                .execute(MyUser.getUid(), otherUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        e -> {
                            LOGGER.error("Failed to add favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        }, () -> {
                            getView().hideAddButton();
                            getView().showSnackBar(R.string.message_added);
                        });
        subscriptions.add(subscription);
    }
}
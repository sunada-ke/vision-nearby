package com.lakeel.altla.vision.nearby.presentation.presenter.user;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsEvent;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsParam;
import com.lakeel.altla.vision.nearby.presentation.analytics.UserParam;
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
    FirebaseAnalytics firebaseAnalytics;

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

    private String userId;

    private String userName;

    private String latitude;

    private String longitude;

    @Inject
    UserPassingPresenter() {
    }

    public void setUserLocationData(String userId, String userName, String latitude, String longitude) {
        this.userId = userId;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void onActivityCreated() {
        // Analytics
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.HISTORY_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.HISTORY_USER_NAME.getValue(), userName);
        firebaseAnalytics.logEvent(AnalyticsEvent.VIEW_HISTORY_ITEM.getValue(), userParam.toBundle());

        Subscription presenceSubscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        subscriptions.add(presenceSubscription);

        Subscription timesSubscription = findTimesUseCase.execute(MyUser.getUid(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times),
                        e -> LOGGER.error("Failed to find times.", e));
        subscriptions.add(timesSubscription);

        Subscription userSubscription = findUserUseCase.execute(userId)
                .map(entity -> userModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showProfile(model),
                        e -> LOGGER.error("Failed to find user.", e));
        subscriptions.add(userSubscription);

        Subscription lineLinkSubscription = findLineLinkUseCase
                .execute(userId)
                .map(lineLinksEntity -> lineLinksEntity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to find LINE link.", e));
        subscriptions.add(lineLinkSubscription);

        Subscription favoriteSubscription = findFavoriteUseCase
                .execute(MyUser.getUid(), userId)
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
        // Analytics
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.FAVORITE_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.FAVORITE_USER_NAME.getValue(), userName);
        firebaseAnalytics.logEvent(AnalyticsEvent.ADD_FAVORITE.getValue(), userParam.toBundle());

        Subscription subscription = saveFavoriteUseCase
                .execute(MyUser.getUid(), userId)
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

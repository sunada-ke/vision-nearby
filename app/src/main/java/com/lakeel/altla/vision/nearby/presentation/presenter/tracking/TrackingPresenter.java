package com.lakeel.altla.vision.nearby.presentation.presenter.tracking;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationDataUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.TrackingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class TrackingPresenter extends BasePresenter<TrackingView> {

    @Inject
    FindLocationDataUseCase findLocationDataUseCase;

    @Inject
    FindLocationUseCase findLocationUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingPresenter.class);

    private String beaconId;

    private String beaconName;

    private GeoLocation geoLocation;

    private boolean isMapReadied;

    private boolean isMenuEnabled;

    @Inject
    TrackingPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = findLocationDataUseCase
                .execute(beaconId)
                .doOnSuccess(entity -> {
                    if (entity != null) getView().showDetectedDate(entity.passingTime);
                })
                .flatMap(new Func1<LocationDataEntity, Single<GeoLocation>>() {
                    @Override
                    public Single<GeoLocation> call(LocationDataEntity entity) {
                        if (entity == null) return Single.just(null);
                        return findLocation(entity.key);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> {
                    if (location == null) {
                        getView().showEmptyView();
                    } else {
                        geoLocation = location;
                        if (isMapReadied) {
                            isMenuEnabled = true;
                            getView().showLocationMap(location);
                            getView().showOptionMenu();
                        }
                    }
                }, e -> LOGGER.error("Failed to find location.", e));
        reusableCompositeSubscription.add(subscription);
    }

    public void setBeaconData(String beaconId, String beaconName) {
        this.beaconId = beaconId;
        this.beaconName = beaconName;
    }

    public void onMapReady() {
        isMapReadied = true;
        if (geoLocation != null) {
            getView().showLocationMap(geoLocation);
        }
    }

    public boolean isMenuEnabled() {
        return isMenuEnabled;
    }

    public void onFindNearbyDeviceMenuClicked() {
        ArrayList<String> beaconIds = new ArrayList<>();
        beaconIds.add(beaconId);
        getView().showFindNearbyDeviceFragment(beaconIds, beaconName);
    }

    public void onDirectionMenuClicked() {
        String latitude = String.valueOf(geoLocation.latitude);
        String longitude = String.valueOf(geoLocation.longitude);
        getView().launchGoogleMapApp(latitude, longitude);
    }

    Single<GeoLocation> findLocation(String uniqueKey) {
        return findLocationUseCase.execute(uniqueKey).subscribeOn(Schedulers.io());
    }
}

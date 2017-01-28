package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindBeaconUseCase {

    @Inject
    FirebaseBeaconsRepository repository;

    @Inject
    FindBeaconUseCase() {
    }

    public Single<Beacon> execute(String beaconId) {
        return repository.findBeacon(beaconId).subscribeOn(Schedulers.io());
    }
}

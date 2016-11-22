package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.ItemsEntity;

import rx.Completable;
import rx.Single;

public interface FirebaseItemsRepository {

    Single<ItemsEntity> findItemsById(String id);

    Single<ItemsEntity> findItemsByName(String name);

    Completable saveItem();

    Single<String> saveBeaconId(String beaconId);
}
package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;

import rx.Single;

public interface PreferenceRepository {

    Single<PreferenceEntity> findPreferences(String userId);

    Single<String> findBeaconId(String userId);

    Single<String> saveBeaconId(String userId);
}
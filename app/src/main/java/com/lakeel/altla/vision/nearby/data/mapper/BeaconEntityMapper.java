package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

public final class BeaconEntityMapper {

    public BeaconEntity map(String userId, String name) {
        BeaconEntity entity = new BeaconEntity();
        entity.userId = userId;
        entity.name = name;
        return entity;
    }
}

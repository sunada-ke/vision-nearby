package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;

public final class LocationDataEntityMapper {

    public LocationDataEntity map(String id) {
        LocationDataEntity entity = new LocationDataEntity();
        entity.id = id;
        return entity;
    }
}

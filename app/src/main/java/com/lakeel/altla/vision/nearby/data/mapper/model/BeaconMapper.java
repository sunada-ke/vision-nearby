package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;

public final class BeaconMapper {

    public Beacon map(BeaconEntity entity, String key) {
        Beacon beacon = new Beacon();
        return beacon;
    }
}

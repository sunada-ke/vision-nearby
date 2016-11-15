package com.lakeel.profile.notification.presentation.presenter.mapper;

import com.lakeel.profile.notification.data.entity.BeaconIdEntity;
import com.lakeel.profile.notification.presentation.presenter.model.BeaconIdModel;

public final class BeaconIdModelMapper {

    public BeaconIdModel map(BeaconIdEntity entity) {
        BeaconIdModel model = new BeaconIdModel();
        model.mNamespaceId = entity.namespaceId;
        model.mInstanceId = entity.instanceId;
        return model;
    }
}
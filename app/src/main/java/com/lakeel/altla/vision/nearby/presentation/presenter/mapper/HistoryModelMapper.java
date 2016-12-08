package com.lakeel.altla.vision.nearby.presentation.presenter.mapper;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity.LocationEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel.LocationTextModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.WeatherModel;

import java.util.List;
import java.util.Map;

public final class HistoryModelMapper {

    public HistoryModel map(@NonNull HistoryEntity historyEntity, @NonNull UserEntity userEntity) {
        HistoryModel model = new HistoryModel();

        model.userId = historyEntity.userId;
        model.name = userEntity.name;
        model.imageUri = userEntity.imageUri;
        model.detectedActivity = historyEntity.userActivity;
        model.passingTime = historyEntity.passingTime;

        LocationEntity locationEntity = historyEntity.location;
        if (locationEntity != null) {
            LocationModel locationModel = new LocationModel();
            locationModel.latitude = locationEntity.latitude;
            locationModel.longitude = locationEntity.longitude;

            Map<String, String> textMap = locationEntity.text;
            if (textMap != null && !textMap.isEmpty()) {
                LocationTextModel locationTextModel = new LocationTextModel();
                locationTextModel.mTextMap = textMap;
                locationModel.mLocationTextModel = locationTextModel;
            }

            model.locationModel = locationModel;
        }

        if (historyEntity.weather != null) {
            List<Integer> conditionList = historyEntity.weather.conditions;
            int[] conditionArray = new int[conditionList.size()];
            for (int count = 0; count < conditionList.size(); count++) {
                conditionArray[count] = conditionList.get(count);
            }
            WeatherModel weather = new WeatherModel();
            weather.conditions = conditionArray;
            weather.humidity = historyEntity.weather.humidity;
            weather.temperature = historyEntity.weather.temperature;
            model.weather = weather;
        }

        return model;
    }
}
package com.lakeel.altla.vision.nearby.data.mapper;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;

import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import static com.lakeel.altla.vision.nearby.data.entity.HistoryEntity.LocationEntity;
import static com.lakeel.altla.vision.nearby.data.entity.HistoryEntity.WeatherEntity;

public final class HistoryEntityMapper {

    public HistoryEntity map(String userId) {
        HistoryEntity entity = new HistoryEntity();
        entity.userId = userId;
        return entity;
    }

    public HistoryEntity map(DetectedActivity detectedActivity) {
        HistoryEntity entity = new HistoryEntity();
        if (detectedActivity != null) {
            entity.userActivity = detectedActivity.getType();
        }
        return entity;
    }

    public HistoryEntity map(Location location) {
        HistoryEntity entity = new HistoryEntity();
        if (location != null) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.latitude = String.valueOf(location.getLatitude());
            locationEntity.longitude = String.valueOf(location.getLongitude());
            entity.location = locationEntity;
        }
        return entity;
    }

    public HistoryEntity map(Weather weather) {
        HistoryEntity entity = new HistoryEntity();
        if (weather != null) {
            entity.weather = new WeatherEntity();
            entity.weather.humidity = weather.getHumidity();
            entity.weather.temperature = weather.getTemperature(Weather.CELSIUS);

            List<Integer> conditions = new ArrayList<>(weather.getConditions().length);
            for (int condition : weather.getConditions()) {
                conditions.add(condition);
            }
            entity.weather.conditions = conditions;
        }
        return entity;
    }
}

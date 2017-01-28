package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.awareness.state.Weather;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveWeatherUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveWeatherUseCase() {
    }

    public Single<HistoryEntity> execute(String uniqueId, String userId, Weather weather) {
        return repository.saveWeather(uniqueId, userId, weather).subscribeOn(Schedulers.io());
    }
}
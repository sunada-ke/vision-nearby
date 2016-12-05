package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveTokensUseCase {

    @Inject
    FirebaseTokensRepository repository;

    @Inject
    SaveTokensUseCase() {
    }

    public Single<String> execute(String userId, String beaconId, String token) {
        return repository.saveToken(userId, beaconId, token);
    }
}

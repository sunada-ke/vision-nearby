package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Observable;

public final class ObserveUserProfileUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    ObserveUserProfileUseCase() {
    }

    public Observable<UserEntity> execute(String userId) {
        return repository.observeUserProfile(userId);
    }
}

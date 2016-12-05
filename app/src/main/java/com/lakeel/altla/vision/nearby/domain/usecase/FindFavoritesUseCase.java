package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.FavoriteEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindFavoritesUseCase {

    @Inject
    FirebaseFavoritesRepository repository;

    @Inject
    FindFavoritesUseCase() {
    }

    public Observable<FavoriteEntity> execute(String userId) {
        return repository.findFavoritesByUserId(userId);
    }
}

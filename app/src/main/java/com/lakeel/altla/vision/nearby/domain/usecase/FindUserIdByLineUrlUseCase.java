package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LINELinkEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindUserIdByLineUrlUseCase {

    @Inject
    FirebaseLINELinksRepository repository;

    @Inject
    FindUserIdByLineUrlUseCase() {
    }

    public Single<LINELinkEntity> execute(String lineUrl) {
        return repository.findUserIdByLINEUrl(lineUrl);
    }

}
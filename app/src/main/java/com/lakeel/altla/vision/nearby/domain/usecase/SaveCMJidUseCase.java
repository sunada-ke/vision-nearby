package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMJidUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    SaveCMJidUseCase() {
    }

    public Single<String> execute(String userId, String jid) {
        return repository.saveCMJid(userId, jid);
    }
}

package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.repository.firebase.NotificationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Notification;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveNotificationUseCase {

    @Inject
    NotificationRepository repository;

    @Inject
    SaveNotificationUseCase() {
    }

    public Completable execute(String token, String title, String message) {
        Notification notification = new Notification();
        notification.token = token;
        notification.title = title;
        notification.message = message;
        notification.registrationTime = ServerValue.TIMESTAMP;
        return repository.save(notification).subscribeOn(Schedulers.io());
    }
}
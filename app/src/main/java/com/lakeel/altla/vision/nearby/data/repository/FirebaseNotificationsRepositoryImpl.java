package com.lakeel.altla.vision.nearby.data.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.data.entity.NotificationEntity;
import com.lakeel.altla.vision.nearby.data.mapper.NotificationEntityMapper;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseNotificationsRepository;

import javax.inject.Inject;

import rx.Single;

public class FirebaseNotificationsRepositoryImpl implements FirebaseNotificationsRepository {

    private DatabaseReference reference;

    private NotificationEntityMapper entityMapper = new NotificationEntityMapper();

    @Inject
    public FirebaseNotificationsRepositoryImpl(String url) {
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<NotificationEntity> saveNotification(String to, String title, String message) {
        return Single.create(subscriber -> {
            NotificationEntity entity = entityMapper.map(to, title, message);

            reference
                    .push()
                    .setValue(entity)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(entity))
                    .addOnFailureListener(subscriber::onError);
        });
    }
}
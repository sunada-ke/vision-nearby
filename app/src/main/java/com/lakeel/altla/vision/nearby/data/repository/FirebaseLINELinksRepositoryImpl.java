package com.lakeel.altla.vision.nearby.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.LINELinkEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.Iterator;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;


public class FirebaseLINELinksRepositoryImpl implements FirebaseLINELinksRepository {

    private static final String URL_KEY = "url";

    private DatabaseReference mReference;

    @Inject
    public FirebaseLINELinksRepositoryImpl(String url) {
        mReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
    }

    @Override
    public Single<String> saveUrl(String url) {
        return Single.create(subscriber -> {
            LINELinkEntity entity = new LINELinkEntity();
            entity.url = url;

            Task<Void> task = mReference
                    .child(MyUser.getUid())
                    .setValue(entity)
                    .addOnSuccessListener(aVoid -> subscriber.onSuccess(url))
                    .addOnFailureListener(subscriber::onError);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }
        });
    }

    @Override
    public Single<LINELinkEntity> findByUserId(String userId) {
        return Single.create(subscriber ->
                mReference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                LINELinkEntity entity = dataSnapshot.getValue(LINELinkEntity.class);
                                subscriber.onSuccess(entity);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    @Override
    public Single<LINELinkEntity> findUserIdByLINEUrl(String url) {
        return Single.create(new Single.OnSubscribe<LINELinkEntity>() {
            @Override
            public void call(SingleSubscriber<? super LINELinkEntity> subscriber) {
                mReference.orderByChild(URL_KEY).equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        Iterator<DataSnapshot> iterator = iterable.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot snapshot = iterator.next();
                            LINELinkEntity entity = snapshot.getValue(LINELinkEntity.class);
                            entity.key = snapshot.getKey();
                            subscriber.onSuccess(entity);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}

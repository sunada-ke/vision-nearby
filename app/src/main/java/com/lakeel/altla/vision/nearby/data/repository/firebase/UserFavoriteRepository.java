package com.lakeel.altla.vision.nearby.data.repository.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.model.FavoriteMapper;
import com.lakeel.altla.vision.nearby.domain.model.Favorite;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class UserFavoriteRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userFavorites";

    private final FavoriteMapper favoriteMapper = new FavoriteMapper();

    private final DatabaseReference reference;

    @Inject
    public UserFavoriteRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Observable<String> findAll(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Boolean isFavorite = (Boolean) snapshot.getValue();
                                if (isFavorite == null || !isFavorite) {
                                    subscriber.onNext(null);
                                } else {
                                    String favoriteUserId = snapshot.getKey();
                                    subscriber.onNext(favoriteUserId);
                                }
                            }
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(final DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    public Single<Favorite> findAll(String myUserId, String favoriteUserId) {
        return Single.create(subscriber ->
                reference
                        .child(myUserId)
                        .child(favoriteUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Boolean isFavorite = (Boolean) dataSnapshot.getValue();
                                if (isFavorite == null || !isFavorite) {
                                    subscriber.onSuccess(null);
                                } else {
                                    subscriber.onSuccess(favoriteMapper.map(dataSnapshot));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Completable save(String myUserId, String favoriteUserId) {
        return Completable.create(subscriber -> {
            Task<Void> task = reference
                    .child(myUserId)
                    .child(favoriteUserId)
                    .setValue(true);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable remove(String myUserId, String favoriteUserId) {
        return Completable.create(subscriber -> {
            Task task = reference
                    .child(myUserId)
                    .child(favoriteUserId)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}

package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.location.Location;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.entity.NearbyHistoryEntity;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.data.mapper.entity.HistoryEntityMapper;
import com.lakeel.altla.vision.nearby.data.mapper.model.NearbyHistoryMapper;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class UserNearbyHistoryRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/userNearbyHistory";

    private static final String USER_ID_KEY = "userId";

    private static final String IS_ENTERED_KEY = "isEntered";

    private static final String USER_ACTIVITY = "userActivity";

    private static final String LOCATION = "location";

    private static final String WEATHER = "weather";

    private final HistoryEntityMapper entityMapper = new HistoryEntityMapper();

    private final NearbyHistoryMapper nearbyHistoryMapper = new NearbyHistoryMapper();

    private final DatabaseReference reference;

    @Inject
    public UserNearbyHistoryRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Observable<NearbyHistory> findAll(String userId) {
        return Observable.create(subscriber -> {
            reference
                    .child(userId)
                    .orderByChild(IS_ENTERED_KEY)
                    .equalTo(true)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                subscriber.onNext(nearbyHistoryMapper.map(snapshot));
                            }
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    public Single<NearbyHistory> find(String userId, String historyId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .child(historyId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                subscriber.onSuccess(nearbyHistoryMapper.map(snapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<NearbyHistory> findLatest(String myUserId, String favoriteUserId) {
        return Single.create(subscriber ->
                reference.child(myUserId)
                        .orderByChild(USER_ID_KEY)
                        .equalTo(favoriteUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                DataSnapshot latestSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    NearbyHistoryEntity entity = snapshot.getValue(NearbyHistoryEntity.class);

                                    if (!entity.isEntered) {
                                        // only enter data.
                                        continue;
                                    }

                                    if (latestSnapshot == null) {
                                        latestSnapshot = snapshot;
                                        continue;
                                    }

                                    NearbyHistoryEntity latestEntity = latestSnapshot.getValue(NearbyHistoryEntity.class);
                                    if ((Long) entity.passingTime > (Long) latestEntity.passingTime) {
                                        // Compare passing time.
                                        latestSnapshot = snapshot;
                                    }
                                }

                                subscriber.onSuccess(nearbyHistoryMapper.map(latestSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<Long> findPassingTimes(String myUserId, String passingUserId) {
        return Single.create(subscriber ->
                reference
                        .child(myUserId)
                        .orderByChild(USER_ID_KEY)
                        .equalTo(passingUserId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long time = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    NearbyHistoryEntity entity = snapshot.getValue(NearbyHistoryEntity.class);
                                    if (entity.isEntered) {
                                        time++;
                                    }
                                }
                                subscriber.onSuccess(time);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<String> save(String myUserId, String passingUserId, RegionState regionState) {
        return Single.create(subscriber -> {
            NearbyHistoryEntity entity = entityMapper.map(passingUserId, regionState);

            DatabaseReference pushedReference = reference.child(myUserId).push();
            String uniqueId = pushedReference.getKey();

            Task<Void> task = pushedReference
                    .setValue(entity);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(uniqueId);
        });
    }

    public Completable saveUserActivity(String uniqueId, String userId, DetectedActivity userActivity) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(USER_ACTIVITY, userActivity);

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable saveLocation(String uniqueId, String userId, Location location) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(LOCATION, location);

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable saveWeather(String uniqueId, String userId, Weather weather) {
        return Completable.create(subscriber -> {
            Map<String, Object> map = new HashMap<>();
            map.put(WEATHER, weather);

            Task<Void> task = reference
                    .child(userId)
                    .child(uniqueId)
                    .updateChildren(map);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onCompleted();
        });
    }

    public Completable remove(String userId, String uniqueKey) {
        return Completable.create(subscriber -> {
            Task task = reference.
                    child(userId)
                    .child(uniqueKey)
                    .removeValue();

            Exception e = task.getException();
            if (e != null) {
                subscriber.onError(e);
            }

            subscriber.onCompleted();
        });
    }
}
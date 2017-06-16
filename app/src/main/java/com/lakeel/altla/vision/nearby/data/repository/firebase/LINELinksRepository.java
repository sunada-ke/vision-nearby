package com.lakeel.altla.vision.nearby.data.repository.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lakeel.altla.vision.nearby.data.execption.DataStoreException;
import com.lakeel.altla.vision.nearby.domain.model.LineLink;

import javax.inject.Inject;

import rx.Single;


public class LINELinksRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/link/line";

    private final DatabaseReference reference;

    @Inject
    LINELinksRepository() {
        this.reference = FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI);
    }

    public Single<LineLink> find(@NonNull String userId) {
        return Single.create(subscriber ->
                reference
                        .child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onSuccess(map(dataSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                subscriber.onError(databaseError.toException());
                            }
                        }));
    }

    public Single<String> save(@NonNull LineLink lineLink) {
        return Single.create(subscriber -> {
            Task<Void> task = reference
                    .child(lineLink.userId)
                    .setValue(lineLink);

            Exception exception = task.getException();
            if (exception != null) {
                throw new DataStoreException(exception);
            }

            subscriber.onSuccess(lineLink.url);
        });
    }

    @Nullable
    private LineLink map(DataSnapshot dataSnapshot) {
        return dataSnapshot.getValue(LineLink.class);
    }
}
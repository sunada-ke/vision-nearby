package com.lakeel.altla.vision.nearby.domain.model;

import com.google.firebase.database.Exclude;

public final class Favorite {

    @Exclude
    public String userId;

    public String favoriteUserId;
}

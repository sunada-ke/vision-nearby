package com.lakeel.altla.vision.nearby.data.mapper;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import java.util.HashMap;
import java.util.Map;

public final class ItemsEntityMapper {

    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap<>();

        MyUser.UserData user = MyUser.getUserData();

        map.put("name", user.mDisplayName);
        map.put("imageUri", user.mImageUri);
        map.put("email", user.mEmail);

        return map;
    }
}
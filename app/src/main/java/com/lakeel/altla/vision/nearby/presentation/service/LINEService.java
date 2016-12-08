package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserIdByLineUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.intent.UriPendingIntent;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LineService extends IntentService {

    @Inject
    FindUserIdByLineUrlUseCase findUserIdByLineUrlUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineService.class);

    public LineService() {
        this(LineService.class.getSimpleName());
    }

    public LineService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Dagger
        ServiceComponent component = DaggerServiceComponent.builder().build();
        component.inject(this);

        String lineUrl = intent.getStringExtra(IntentKey.LINE_URL.name());

        LOGGER.info("LINE URL was found:URL=" + lineUrl);

        findUserIdByLineUrlUseCase
                .execute(lineUrl)
                .flatMap(entity -> findUser(entity.key))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    String title = getString(R.string.notification_title_line_user_found);
                    String message = getString(R.string.notification_message_user_using_line, entity.name);

                    UriPendingIntent creator = new UriPendingIntent(getApplicationContext(), Uri.parse(lineUrl));
                    PendingIntent pendingIntent = creator.create();

                    LocalNotification notification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
                    notification.show();
                }, e -> LOGGER.error("Failed to notify a LINE notification.", e));
    }

    Single<UserEntity> findUser(String jsonKey) {
        return findUserUseCase.execute(jsonKey).subscribeOn(Schedulers.io());
    }
}

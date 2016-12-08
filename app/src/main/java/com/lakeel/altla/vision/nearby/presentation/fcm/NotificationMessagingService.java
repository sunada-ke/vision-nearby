package com.lakeel.altla.vision.nearby.presentation.fcm;

import android.app.PendingIntent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lakeel.altla.vision.nearby.presentation.intent.DefaultPendingIntent;
import com.lakeel.altla.vision.nearby.presentation.notification.LocalNotification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationMessagingService extends FirebaseMessagingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationMessagingService.class);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        RemoteMessage.Notification payload = remoteMessage.getNotification();
        if (payload == null) {
            LOGGER.error("Notification payload was empty.");
            return;
        }

        DefaultPendingIntent defaultPendingIntent = new DefaultPendingIntent(getApplicationContext());
        PendingIntent pendingIntent = defaultPendingIntent.create();

        String title = payload.getTitle();
        String message = payload.getBody();

        LocalNotification localNotification = new LocalNotification(getApplicationContext(), title, message, pendingIntent);
        localNotification.show();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        LOGGER.debug("This message has been deleted.");
    }

    @Override
    public void onSendError(String msgId, Exception e) {
        super.onSendError(msgId, e);
        LOGGER.error("FCM has failed to send a notification message.", e);
    }
}

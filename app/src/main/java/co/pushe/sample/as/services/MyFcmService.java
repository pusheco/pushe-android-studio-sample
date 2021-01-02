package co.pushe.sample.as.services;



import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.pushe.plus.Pushe;
import co.pushe.plus.fcm.FcmHandler;
import co.pushe.plus.fcm.PusheFCM;


public class MyFcmService extends FirebaseMessagingService {

    private final FcmHandler fcmHandler = Pushe.getPusheService(PusheFCM.class).getFcmHandler();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("P_AS", "Message received: \n" + remoteMessage.getData());
        if (fcmHandler != null && fcmHandler.onMessageReceived(remoteMessage)) {
            // Message is for Pushe
            return;
        }
        super.onMessageReceived(remoteMessage);

        // Handle Firebase message
    }

    @Override
    public void onNewToken(String s) {
        if (fcmHandler != null) {
            fcmHandler.onNewToken(s);
        }
        super.onNewToken(s);

        // Token is refreshed
    }

    @Override
    public void onMessageSent(String s) {
        if (fcmHandler != null) {
            fcmHandler.onMessageSent(s);
        }
        super.onMessageSent(s);

        // Message sent
    }

    @Override
    public void onDeletedMessages() {
        if (fcmHandler != null) {
            fcmHandler.onDeletedMessages();
        }
        super.onDeletedMessages();

        // Message was deleted
    }

    @Override
    public void onSendError(String s, Exception e) {
        if (fcmHandler != null) {
            fcmHandler.onSendError(s, e);
        }
        super.onSendError(s, e);

        // Error sent
    }
}

package co.ronash.pushesample.as.services;



import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import co.pushe.plus.Pushe;


public class MyFcmService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("P_AS", "Message received: \n" + remoteMessage.getData());
        if (Pushe.getFcmHandler().onMessageReceived(remoteMessage)) {
            // Message is for Pushe
            return;
        }
        super.onMessageReceived(remoteMessage);

        // Handle Firebase message
    }

    @Override
    public void onNewToken(String s) {
        Pushe.getFcmHandler().onNewToken(s);
        super.onNewToken(s);

        // Token is refreshed
    }

    @Override
    public void onMessageSent(String s) {
        Pushe.getFcmHandler().onMessageSent(s);
        super.onMessageSent(s);

        // Message sent
    }

    @Override
    public void onDeletedMessages() {
        Pushe.getFcmHandler().onDeletedMessages();
        super.onDeletedMessages();

        // Message was deleted
    }

    @Override
    public void onSendError(String s, Exception e) {
        Pushe.getFcmHandler().onSendError(s, e);
        super.onSendError(s, e);

        // Error sent
    }
}

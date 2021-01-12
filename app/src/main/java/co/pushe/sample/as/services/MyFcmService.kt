package co.pushe.sample.`as`.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import co.pushe.plus.fcm.FcmHandler
import co.pushe.plus.Pushe
import co.pushe.plus.fcm.PusheFCM
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class MyFcmService : FirebaseMessagingService() {
    private val fcmHandler: FcmHandler? = Pushe.getPusheService(PusheFCM::class.java)?.fcmHandler
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("P_AS", """
     Message received: 
     ${remoteMessage.data}
     """.trimIndent())
        if (fcmHandler != null && fcmHandler.onMessageReceived(remoteMessage)) {
            // Message is for Pushe. It'll be handled by itself
            return
        }
        super.onMessageReceived(remoteMessage)

        // Handle Firebase message
    }

    override fun onNewToken(s: String) {
        fcmHandler?.onNewToken(s)
        super.onNewToken(s)

        // Token is refreshed
    }

    override fun onMessageSent(s: String) {
        fcmHandler?.onMessageSent(s)
        super.onMessageSent(s)

        // Message sent
    }

    override fun onDeletedMessages() {
        fcmHandler?.onDeletedMessages()
        super.onDeletedMessages()

        // Message was deleted
    }

    override fun onSendError(s: String, e: Exception) {
        fcmHandler?.onSendError(s, e)
        super.onSendError(s, e)

        // Error sent
    }
}
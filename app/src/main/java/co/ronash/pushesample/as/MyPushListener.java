package co.ronash.pushesample.as;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

/**
 * Created on 16-05-09, 6:20 PM.
 *
 * @author Mahdi Malvandi
 */
public class MyPushListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        if (message != null && message.length() > 0) {
            android.util.Log.i("Pushe", "Custom json Message: " + message.toString());
        }
    }
}

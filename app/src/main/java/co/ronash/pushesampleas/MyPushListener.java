package co.ronash.pushesampleas;

import org.json.JSONObject;

import co.ronash.pushe.PusheListenerService;

/**
 * Created on 16-05-09, 6:20 PM.
 *
 * @author Akram Shokri
 */
public class MyPushListener extends PusheListenerService {
    @Override
    public void onMessageReceived(JSONObject message,JSONObject content) {
        android.util.Log.i("Pushe","Custom json Message: "+ message.toString());
        //    your code
    }
}

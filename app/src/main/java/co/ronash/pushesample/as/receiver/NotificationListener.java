package co.ronash.pushesample.as.receiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.ronash.pushe.PusheListenerService;
import co.ronash.pushesample.as.eventbus.MessageEvent;

/**
 * Created on 16-05-09, 6:20 PM.
 *
 * Note: {@link PusheListenerService} extends {@link android.app.Service} class.
 *
 * @author Mahdi Malvandi
 */
public class NotificationListener extends PusheListenerService {
    @Override
    public void onMessageReceived(final JSONObject message, JSONObject content) {
        Map<String, String> data = new HashMap<>();
        data.put("event", "notification_received");
        data.put("notification", content.toString());
        if (message != null && !message.toString().isEmpty()) {
            data.put("custom_content", content.toString());
        }

        // Send to who wants it
        EventBus.getDefault().post(new MessageEvent(data.toString()));
    }
}

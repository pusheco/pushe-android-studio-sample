package co.ronash.pushesample.as.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import co.ronash.pushe.Constants;
import co.ronash.pushesample.as.eventbus.MessageEvent;

/**
 * Created on 2017-09-05, 4:07 PM.
 *
 * <b>NOTE: Current version does not send notification data to the click dismiss receiver. Feature will be included in next version.</b>
 *
 * @author Mahdi Malvandi
 */

public class NotificationClickDismissReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Map<String, String> data = new HashMap<>();
        if (action == null) return;
        if(action.equals(context.getPackageName()+".pusheco.NOTIF_CLICKED")){
            data.put("event", "notification_clicked");
            EventBus.getDefault().post(new MessageEvent(data.toString()));
        }
        else if(action.equals(context.getPackageName()+".pusheco.NOTIF_DISMISSED")){
            data.put("event", "notification_dismissed");
            EventBus.getDefault().post(new MessageEvent(data.toString()));
        }
        else if(action.equals(context.getPackageName()+".pusheco.NOTIF_BTN_CLICKED")){
            data.put("event", "notification_button_clicked");
            EventBus.getDefault().post(new MessageEvent(data.toString()));
        }
    }
}

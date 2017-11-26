package co.ronash.pushesample.as;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.logging.Logger;

import co.ronash.pushe.Constants;

/**
 * Created on 2017-09-05, 4:07 PM.
 *
 * @author Akram Shokri
 */

public class NotifBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_CLICKED")){
            Log.i("Pushe", "Broadcast co.ronash.pushe.NOTIF_CLICKED received");
            //add your logic here
        }
        else if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_DISMISSED")){
            Log.i("Pushe", "Broadcast co.ronash.pushe.NOTIF_DISMISSED received");
            //add your logic here
        }
        else if(intent.getAction().equals(context.getPackageName()+"co.ronash.pushe.NOTIF_BTN_CLICKED")){
            String btnId = intent.getStringExtra("pushe_notif_btn_id");
            Log.i("Pushe", "Broadcast co.ronash.pushe.NOTIF_BTN_CLICKED received. BtnId =  " + btnId);
            //add your logic here
        }
    }
}

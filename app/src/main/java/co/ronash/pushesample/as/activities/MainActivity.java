package co.ronash.pushesample.as.activities;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.pushe.plus.Pushe;
import co.pushe.plus.analytics.PusheAnalytics;
import co.pushe.plus.notification.NotificationButtonData;
import co.pushe.plus.notification.NotificationData;
import co.pushe.plus.notification.PusheNotification;
import co.pushe.plus.notification.PusheNotificationListener;
import co.pushe.plus.notification.UserNotification;
import co.ronash.pushesample.as.R;
import co.ronash.pushesample.as.eventbus.MessageEvent;
import co.ronash.pushesample.as.utils.Stuff;

import static co.ronash.pushesample.as.utils.Stuff.addText;
import static co.ronash.pushesample.as.utils.Stuff.alert;
import static co.ronash.pushesample.as.utils.Stuff.prompt;

/**
 * For further information Go to <a href="https://pushe.co/docs">Docs</a>
 *
 * @author Mahdi Malvandi
 */
@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.statusContainer)
    ScrollView scroll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        try {
            toolbar.setSubtitle("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        status.setText("Click an action to test it.");

        setupList();

        // Clear on long click
        status.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                status.setText("Click an action to test it.\n");
                return true;
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        Pushe.getPusheService(PusheNotification.class).setNotificationListener(new PusheNotificationListener() {
            @Override
            public void onNotification(@NonNull NotificationData notification) {
                addText(status, "Listener on receiving notification triggered");
                scroll.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onCustomContentNotification(@NonNull Map<String, Object> customContent) {
                addText(status, "Listener on receiving notification with custom content triggered");
                scroll.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onNotificationClick(@NonNull NotificationData notification) {
                addText(status, "Listener on clicking on notification triggered");
                scroll.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onNotificationDismiss(@NonNull NotificationData notification) {
                addText(status, "Listener on dismissing notification triggered");
                scroll.fullScroll(View.FOCUS_DOWN);
            }

            @Override
            public void onNotificationButtonClick(@NonNull NotificationButtonData button, @NonNull NotificationData notification) {
                addText(status, "Listener on clicking on notification button triggered");
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * Set up recyclerView and add items to it.
     * Checkout class {@link Stuff} to see all util methods.
     */
    private void setupList() {
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new Adapter(
                Stuff.listOf(
                        "IDs",
                        "Custom Id",
                        "Phone Number",
                        "Email",
                        "Modules initialization status",
                        "Device registration status",
                        "Topic",
                        "Tag (name:value)",
                        "Analytics: Event",
                        "Analytics: E-commerce",
                        "Notification: AndroidId",
                        "Notification: GoogleAdId",
                        "Notification: CustomId"
                ),
                handleItemClick()));
    }

    /**
     * Each clicked item have to do something when clicked.
     * Adapter takes an interface and calls it when an item was clicked.
     * This function does the work and returns an interface.
     */
    private ItemClickListener handleItemClick() {
        return new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case 0:
                        alert(MainActivity.this, "IDs", "AndroidId:\n" + Pushe.getAndroidId() + "\nGoogleAdId:\n" + Pushe.getGoogleAdvertisingId());
                        break;
                    case 1:
                        prompt(MainActivity.this, "New custom id", "current custom id:" + Pushe.getCustomId(), new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                Pushe.setCustomId(s);
                                addText(status, "Custom id is: " + Pushe.getCustomId());
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;
                    case 2:
                        prompt(MainActivity.this, "New phone number", "current phone number is:" + Pushe.getUserPhoneNumber(), new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                Pushe.setUserPhoneNumber(s);
                                addText(status, "phone number is: " + Pushe.getUserPhoneNumber());
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                        break;
                    case 3:
                        prompt(MainActivity.this, "New Email", "current email is:" + Pushe.getUserEmail(), new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                Pushe.setUserEmail(s);
                                addText(status, "user email is: " + Pushe.getUserEmail());
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;
                    case 4:
                        addText(status, "modules initialized: " + Pushe.isInitialized());
                        scroll.fullScroll(View.FOCUS_DOWN);
                        break;
                    case 5:
                        addText(status, "Device registered: " + Pushe.isRegistered());
                        scroll.fullScroll(View.FOCUS_DOWN);
                        break;

                    case 6:
                        prompt(MainActivity.this, "Topic", "Topics:[" + Pushe.getSubscribedTopics().toString() + "]\nEnter topic name to subscribe or unsubscribe", "topic name", "Subscribe", "Unsubscribe", new Stuff.Callback<String>() {
                            @Override
                            public void onPositiveButtonClicked(String s) {
                                Pushe.subscribeToTopic(s);
                                addText(status, "Subscribe to topic: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }

                            @Override
                            public void onNegativeButtonClicked(String s) {
                                Pushe.unsubscribeFromTopic(s);
                                addText(status, "Unsubscribe from topic: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;

                    case 7:
                        prompt(MainActivity.this, "Tags", "Tags:" + Pushe.getSubscribedTags().toString() + "\nTag in name:value format (add)\n Tag in name1,name2 format (remove)", "topic name", "Add", "Remove", new Stuff.Callback<String>() {
                            @Override
                            public void onPositiveButtonClicked(String s) {
                                if (!s.contains(":")) return;
                                String[] keyValue = s.split(":");
                                if (keyValue.length != 2) return;
                                Map map = new HashMap();
                                map.put(keyValue[0], keyValue[1]);
                                Pushe.addTags(map);
                                addText(status, "Tag \'" + keyValue[0] + "\' added ");
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }

                            @Override
                            public void onNegativeButtonClicked(String s) {
                                String[] keyList = s.split(",");
                                if (keyList.length == 0) return;
                                Pushe.removeTags(Arrays.asList(keyList));
                                addText(status, "Tags '" + s + "' removed");
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;

                    case 8:
                        prompt(MainActivity.this, "Event", "Type event name to send", new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                Pushe.getPusheService(PusheAnalytics.class).sendEvent(s);
                                addText(status, "Sending event: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);

                            }
                        });

                        break;

                    case 9:
                        prompt(MainActivity.this, "E-commerce", "Enter value in name:price format to send data", new Consumer<String>() {
                            @Override
                            public void accept(String s) {
                                if (!s.contains(":")) return;
                                String[] keyValue = s.split(":");
                                if (keyValue.length != 2) return;
                                try {
                                    Pushe.getPusheService(PusheAnalytics.class).sendEcommerceData(keyValue[0], Double.parseDouble(keyValue[1]));
                                    addText(status, "Sending E-commerce data with name "+keyValue[0]+" and price "+keyValue[1]);
                                    scroll.fullScroll(View.FOCUS_DOWN);
                                } catch (NumberFormatException e) {
                                    addText(status, "Enter valid price (price should be double)");
                                    scroll.fullScroll(View.FOCUS_DOWN);
                                }
                            }
                        });

                        break;
                    case 10:
                        prompt(MainActivity.this, "Notification", "Enter androidId to send a notification to that user", "androidId",  "Send to ...","Send to me", new Stuff.Callback<String>() {
                            @Override
                            public void onPositiveButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withAndroidId(s);
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to AndroidId: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }

                            @Override
                            public void onNegativeButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withAndroidId(Pushe.getAndroidId());
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to this device");
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;

                    case 11:
                        prompt(MainActivity.this, "Notification", "Enter GoogleAdId to send a notification to that user", "GoogleAdId", "Send to ...","Send to me", new Stuff.Callback<String>() {
                            @Override
                            public void onPositiveButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withAdvertisementId(s);
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to GoogleAdId: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }

                            @Override
                            public void onNegativeButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withAdvertisementId(Pushe.getGoogleAdvertisingId());
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to this device");
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });

                        break;

                    case 12:
                        prompt(MainActivity.this, "Notification", "Enter custom id to send a notification to that user", "custom id", "Send to ...","Send to me", new Stuff.Callback<String>() {
                            @Override
                            public void onPositiveButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withCustomId(s);
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to custom id: " + s);
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }

                            @Override
                            public void onNegativeButtonClicked(String s) {
                                UserNotification userNotification = UserNotification.withCustomId(Pushe.getCustomId());
                                userNotification.setTitle("title1");
                                userNotification.setContent("content1");
                                Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                addText(status, "Sending notification to this device");
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                }
            }
        };
    }

    // region EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        addText(status, event.getMessage());
        scroll.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    // endregion

    // region List

    // List adapter
    class Adapter extends RecyclerView.Adapter<Holder> {

        private List<String> dataSet;
        private ItemClickListener listener;

        Adapter(List<String> dataSet, ItemClickListener listener) {
            this.dataSet = dataSet;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, int i) {
            final int position = i;
            holder.action.setText(dataSet.get(position));
            holder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }
    }

    // List view holder
    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView action;

        Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    // endregion

}

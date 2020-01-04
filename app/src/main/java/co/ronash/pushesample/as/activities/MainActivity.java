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
            toolbar.setSubtitle("v" + getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " || click each icon to see info");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        status.setText("Click an action to test it.\nClick the info to see information.\n");

        setupList();

        // Clear on long click
        status.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                status.setText("Click an action to test it.\nClick the info to see information.\n");
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
                        "Manually Initialize Pushe",
                        "Check initialized",
                        "Get PusheId",
                        "Subscribe to topic",
                        "Unsubscribe from topic",
                        "Send notification to user",
                        "Send advanced notification to user",
                        "Send event"
                ),
                handleItemClick(),
                handleInfoClicked()
        ));
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
                    case 1: // Check init
                        addText(status, "Pushe initialized: " + Pushe.isInitialized());
                        scroll.fullScroll(View.FOCUS_DOWN);
                        break;
                    case 2: // PusheId
                        addText(status, "PusheId is: " + Pushe.getPusheId());
                        scroll.fullScroll(View.FOCUS_DOWN);
                        break;
                    case 3: // Topic
                        Stuff.prompt(MainActivity.this,
                                "Subscribe to Topic",
                                "Enter topic name (Must be english character)",
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        Pushe.subscribeToTopic(s);
                                        addText(status, "Subscribe to topic: " + s);
                                        scroll.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                        break;
                    case 4: // Unsubscribe
                        Stuff.prompt(MainActivity.this,
                                "Unsubscribe from Topic",
                                "Enter topic name",
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        Pushe.unsubscribeFromTopic( s);
                                        addText(status, "Unsubscribe from topic: " + s);
                                        scroll.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                        break;
                    case 5: // Send to user
                        Stuff.prompt(MainActivity.this,
                                "Send simple notification to user",
                                "Enter androidId\nMessage:{title:title1, content:content1}",
                                Pushe.getAndroidId(),
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String androidId) {
                                        UserNotification userNotification = UserNotification.withAndroidId(androidId);
                                        userNotification.setTitle("title1");
                                        userNotification.setContent("content1");
                                        Pushe.getPusheService(PusheNotification.class).sendNotificationToUser(userNotification);
                                        addText(status, "Sending simple notification to user.\ntitle: title1\ncontent: content1\nAndroidId: " + androidId);
                                        scroll.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                        break;
                    case 7: // send event
                        Stuff.prompt(MainActivity.this,
                                "Send event",
                                "Enter event name to send",
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String event) {
                                        Pushe.getPusheService(PusheAnalytics.class).sendEvent(event);
                                        addText(status, "Sending event: " + event);
                                        scroll.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                }
            }
        };
    }

    private ItemClickListener handleInfoClicked() {
        return new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case 0: // Initialize manually
                        Stuff.alert(MainActivity.this,
                                "initialize(context,showDialog)",
                                "Register this device to pushe and get FCM token from server.\nSecond argument: If googlePlay does not exist or not updated it will show a dialog to install (if set to true)");
                        break;
                    case 1: // Check initialized
                        Stuff.alert(MainActivity.this,
                                "isPusheInitialized(context)",
                                "Returns true if registration is successful and token is saved.");
                        break;
                    case 2: // getPusheId
                        Stuff.alert(MainActivity.this,
                                "getPusheId(context)",
                                "Returns a unique id according to androidId and googleAdId which can be used to identify device.");
                        break;
                    case 3: // subscribe
                        Stuff.alert(MainActivity.this,
                                "subscribe(topicName)",
                                "If you want to add user to a specific group (for example premium), you can subscribe them into a topic.");
                        break;
                    case 4: // unsubscribe
                        Stuff.alert(MainActivity.this,
                                "unsubscribe(topicName)",
                                "If you want to remove user from a specific group (for example premium), you can unsubscribe them from that topic.");
                        break;
                    case 5: // send to user
                        Stuff.alert(MainActivity.this,
                                "sendSimpleNotifToUser(context,pusheId,title,content)",
                                "Having a pusheId of a device, you can send title and content to that device programmatically.");
                        break;
                    case 6: // send advanced to user
                        Stuff.alert(MainActivity.this,
                                "sendAdvancedNotifToUser(context,pusheId,notificationInJsonFormatAsString)",
                                "Having a pusheId of a device, you can send complete notification in json format (jsonObject.toString()) to that device programmatically.");
                        break;
                    case 7: // send event
                        Stuff.alert(MainActivity.this,
                                "sendEvent(context,eventName)",
                                "If anything has happened in the device, you can send it using this function.");

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
        private ItemClickListener listener, infoListener;

        Adapter(List<String> dataSet, ItemClickListener listener, ItemClickListener infoListener) {
            this.dataSet = dataSet;
            this.listener = listener;
            this.infoListener = infoListener;
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
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    infoListener.onItemClick(view, position);
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

        @BindView(R.id.text) TextView action;
        @BindView(R.id.info) ImageView info;

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

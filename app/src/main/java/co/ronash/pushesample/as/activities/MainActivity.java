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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ronash.pushe.Pushe;
import co.ronash.pushe.j.c;
import co.ronash.pushesample.as.R;
import co.ronash.pushesample.as.eventbus.MessageEvent;
import co.ronash.pushesample.as.utils.Stuff;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        try {
            toolbar.setSubtitle("version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        status.setText("Click an action to test it.\nHold item to see information.");

        Pushe.initialize(this, true);

        setupList();

        // Clear on long click
        status.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                status.setText("");
                return true;
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
                handleItemLongClick()
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
                    case 0: // Initialize
                        status.setText("Initializing Pushe");
                        Pushe.initialize(MainActivity.this, true);
                        break;
                    case 1: // Check init
                        status.setText("Pushe initialized: " + Pushe.isPusheInitialized(MainActivity.this));
                        break;
                    case 2: // PusheId
                        status.setText("PusheId is: " + Pushe.getPusheId(MainActivity.this));
                        break;
                    case 3: // Topic
                        Stuff.prompt(MainActivity.this,
                                "Subscribe to Topic",
                                "Enter topic name (Must be english character)",
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String s) {
                                        Pushe.subscribe(MainActivity.this, s);
                                        status.setText("Subscribe to topic: " + s);
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
                                        Pushe.unsubscribe(MainActivity.this, s);
                                        status.setText("Unsubscribe from topic: " + s);
                                    }
                                });
                        break;
                    case 5: // Send to user
                        Stuff.prompt(MainActivity.this,
                                "Send simple notification to user",
                                "Enter pusheId\nMessage:{title:title1, content:content1}",
                                Pushe.getPusheId(MainActivity.this),
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String pusheId) {
                                        Pushe.sendSimpleNotifToUser(MainActivity.this, pusheId, "title1", "content1");
                                        status.setText("Sending simple notification to user.\ntitle: title1\ncontent: content1\nPusheId: " + pusheId);
                                    }
                                });
                        break;
                    case 6: // send advanced to user
                        Stuff.prompt(MainActivity.this,
                                "Send advanced notification to user",
                                "Enter pusheId\nMessage:{title:title1, content:content1}",
                                Pushe.getPusheId(MainActivity.this),
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String pusheId) {
                                        try {
                                            JSONObject object = new JSONObject();
                                            object.put("title", "title1");
                                            object.put("content", "content1");
                                            Pushe.sendAdvancedNotifToUser(MainActivity.this, pusheId, object.toString());
                                            status.setText("Sending advanced notification:\nNotification: " + object.toString() + "\nPusheId: " + pusheId);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );
                        break;
                    case 7: // send event
                        Stuff.prompt(MainActivity.this,
                                "Send event",
                                "Enter event name to send",
                                new Consumer<String>() {
                                    @Override
                                    public void accept(String event) {
                                        Pushe.sendEvent(MainActivity.this, event);
                                        status.setText("Sending event: " + event);
                                    }
                                });
                }
            }
        };
    }

    private ItemClickListener handleItemLongClick() {
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
        status.setText(event.getMessage());
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
        private ItemClickListener listener, holdListener;

        Adapter(List<String> dataSet, ItemClickListener listener, ItemClickListener holdListener) {
            this.dataSet = dataSet;
            this.listener = listener;
            this.holdListener = holdListener;
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holdListener.onItemClick(view, position);
                    return true;
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

        TextView action;

        Holder(@NonNull View itemView) {
            super(itemView);
            action = itemView.findViewById(R.id.text);
        }
    }

    interface ItemClickListener {
        void onItemClick(View v, int position);
    }

    // endregion

}

package co.pushe.sample.`as`.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Consumer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import co.pushe.plus.Pushe
import co.pushe.plus.analytics.PusheAnalytics
import co.pushe.plus.inappmessaging.InAppMessage
import co.pushe.plus.inappmessaging.PusheInAppMessaging
import co.pushe.plus.inappmessaging.PusheInAppMessagingListener
import co.pushe.plus.notification.*
import co.pushe.sample.`as`.R
import co.pushe.sample.`as`.eventbus.MessageEvent
import co.pushe.sample.`as`.utils.Stuff
import co.pushe.sample.`as`.utils.Stuff.addText
import co.pushe.sample.`as`.utils.Stuff.alert
import co.pushe.sample.`as`.utils.Stuff.prompt
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * For further information Go to [Docs](https://pushe.co/docs)
 *
 * @author Mahdi Malvandi
 */
@Suppress("unused")
@SuppressLint("SetTextI18n,NonConstantResourceId")
class MainActivity : AppCompatActivity() {
    private var inAppMessaging: PusheInAppMessaging? = null

    @BindView(R.id.status)
    lateinit var status: TextView

    @BindView(R.id.list)
    lateinit var list: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.statusContainer)
    lateinit var scroll: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        inAppMessaging = Pushe.getPusheService(PusheInAppMessaging::class.java)
        try {
            toolbar.subtitle = "v" + packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        status.text = "Click an action to test it."
        setupList()

        // Clear on long click
        status.setOnLongClickListener {
            status.text = "Click an action to test it.\n"
            true
        }
        status.setOnClickListener { scroll.fullScroll(View.FOCUS_DOWN) }

        // Notification listener
        val pnotif: PusheNotification? = Pushe.getPusheService(PusheNotification::class.java)
        if (pnotif != null) {
            Pushe.getPusheService(PusheNotification::class.java)?.setNotificationListener(object : PusheNotificationListener {
                override fun onNotification(notification: NotificationData) {
                    addText(scroll, status, """
     Listener on receiving notification triggered
     ${notification.title}
     """.trimIndent())
                }

                override fun onCustomContentNotification(customContent: Map<String, Any>) {
                    addText(scroll, status, "Listener on receiving notification with custom content triggered")
                }

                override fun onNotificationClick(notification: NotificationData) {
                    addText(scroll, status, "Listener on clicking on notification triggered")
                }

                override fun onNotificationDismiss(notification: NotificationData) {
                    addText(scroll, status, "Listener on dismissing notification triggered")
                }

                override fun onNotificationButtonClick(button: NotificationButtonData, notification: NotificationData) {
                    addText(scroll, status, "Listener on clicking on notification button triggered")
                }
            })
        }
        // Piam listener
        Pushe.getPusheService(PusheInAppMessaging::class.java)?.setInAppMessagingListener(object : PusheInAppMessagingListener {
            override fun onInAppMessageReceived(inAppMessage: InAppMessage) {
                addText(scroll, status, """
           InAppMessage received:
           Title: ${inAppMessage.title}
           Content: ${inAppMessage.content}
           """.trimIndent())
            }

            override fun onInAppMessageTriggered(inAppMessage: InAppMessage) {}
            override fun onInAppMessageClicked(inAppMessage: InAppMessage) {}
            override fun onInAppMessageDismissed(inAppMessage: InAppMessage) {}
            override fun onInAppMessageButtonClicked(inAppMessage: InAppMessage, piamButtonIndex: Int) {}
        })
    }

    /**
     * Set up recyclerView and add items to it.
     * Checkout class [Stuff] to see all util methods.
     */
    private fun setupList() {
        list.setHasFixedSize(true)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = Adapter(
          listOf(
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
            "Notification: CustomId",
            "Piam: Dismiss All InApps",
            "Piam: Trigger event"
          ),
          handleItemClick())
    }

    /**
     * Each clicked item have to do something when clicked.
     * Adapter takes an interface and calls it when an item was clicked.
     * This function does the work and returns an interface.
     */
    private fun handleItemClick(): ItemClickListener {
        return object : ItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                when (position) {
                    0 -> alert(this@MainActivity, "IDs", """
     AndroidId:
     ${Pushe.getDeviceId()}
     GoogleAdId:
     ${Pushe.getAdvertisingId()}
     """.trimIndent())
                    1 -> prompt(this@MainActivity, "New custom id", "current custom id:" + Pushe.getCustomId(), Consumer { s ->
                        Pushe.setCustomId(s)
                        addText(scroll, status, "Custom id is: " + Pushe.getCustomId())
                    })
                    2 -> prompt(this@MainActivity, "New phone number", "current phone number is:" + Pushe.getUserPhoneNumber(), Consumer { s ->
                        Pushe.setUserPhoneNumber(s)
                        addText(scroll, status, "phone number is: " + Pushe.getUserPhoneNumber())
                    })
                    3 -> prompt(this@MainActivity, "New Email", "current email is:" + Pushe.getUserEmail(), Consumer { s ->
                        Pushe.setUserEmail(s)
                        addText(scroll, status, "user email is: " + Pushe.getUserEmail())
                    })
                    4 -> addText(scroll, status, "modules initialized: " + Pushe.isInitialized())
                    5 -> addText(scroll, status, "Device registered: " + Pushe.isRegistered())
                    6 -> prompt(this@MainActivity, "Topic", """
     Topics:[${Pushe.getSubscribedTopics()}]
     Enter topic name to subscribe or unsubscribe
     """.trimIndent(), "topic name", "Subscribe", "Unsubscribe", object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            Pushe.subscribeToTopic(t)
                            addText(scroll, status, "Subscribe to topic: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            Pushe.unsubscribeFromTopic(t)
                            addText(scroll, status, "Unsubscribe from topic: $t")
                        }
                    })
                    7 -> prompt(this@MainActivity, "Tags", """Tags:${Pushe.getSubscribedTags()}
Tag in name:value format (add)
 Tag in name1,name2 format (remove)""", "topic name", "Add", "Remove", object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            if (!t.contains(":")) return
                            val keyValue = t.split(":").toTypedArray()
                            if (keyValue.size != 2) return
                            val map: MutableMap<String, String> = mutableMapOf()
                            map[keyValue[0]] = keyValue[1]
                            Pushe.addTags(map)
                            addText(scroll, status, "Tag '" + keyValue[0] + "' added ")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val keyList = t.split(",").toTypedArray()
                            if (keyList.isEmpty()) return
                            Pushe.removeTags(listOf(*keyList))
                            addText(scroll, status, "Tags '$t' removed")
                        }
                    })
                    8 -> prompt(this@MainActivity, "Event", "Type event name to send", Consumer { s ->
                        Pushe.getPusheService(PusheAnalytics::class.java)?.sendEvent(s)
                        addText(scroll, status, "Sending event: $s")
                    })
                    9 -> prompt(this@MainActivity, "E-commerce", "Enter value in name:price format to send data", Consumer { s ->
                        if (!s.contains(":")) return@Consumer
                        val keyValue = s.split(":").toTypedArray()
                        if (keyValue.size != 2) return@Consumer
                        try {
                            Pushe.getPusheService(PusheAnalytics::class.java)?.sendEcommerceData(keyValue[0], keyValue[1].toDouble())
                            addText(scroll, status, "Sending E-commerce data with name " + keyValue[0] + " and price " + keyValue[1])
                        } catch (e: NumberFormatException) {
                            addText(scroll, status, "Enter valid price (price should be double)")
                        }
                    })
                    10 -> prompt(this@MainActivity, "Notification", "Enter androidId to send a notification to that user", "androidId", "Send to ...", "Send to me", object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withDeviceId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to AndroidId: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withDeviceId(Pushe.getDeviceId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to this device")
                        }
                    })
                    11 -> prompt(this@MainActivity, "Notification", "Enter GoogleAdId to send a notification to that user", "GoogleAdId", "Send to ...", "Send to me", object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withAdvertisementId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to GoogleAdId: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withAdvertisementId(Pushe.getAdvertisingId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to this device")
                        }
                    })
                    12 -> prompt(this@MainActivity, "Notification", "Enter custom id to send a notification to that user", "custom id", "Send to ...", "Send to me", object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withCustomId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to custom id: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withCustomId(Pushe.getCustomId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)?.sendNotificationToUser(userNotification)
                            addText(scroll, status, "Sending notification to this device")
                        }
                    })
                    13 -> {
                        if (inAppMessaging == null) return
                        inAppMessaging!!.dismissShownInApp()
                    }
                    14 -> {
                        if (inAppMessaging == null) return
                        prompt(this@MainActivity, "InAppMessaging", "Enter an event", "", Consumer { s ->
                            if (s.isEmpty() || listOf("%", "&", "@").contains(s) || s.length > 10) {
                                Toast.makeText(this@MainActivity, "Invalid event name", Toast.LENGTH_SHORT).show()
                            } else {
                                addText(scroll, status, "Triggering event: $s")
                                inAppMessaging!!.triggerEvent(s)
                            }
                        })
                    }
                }
            }
        }
    }

    // region EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        addText(scroll, status, event.message)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    // endregion
    // region List
    // List adapter
    internal class Adapter(private val dataSet: List<String>, private val listener: ItemClickListener) : RecyclerView.Adapter<Holder?>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
            return Holder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, viewGroup, false))
        }

        override fun onBindViewHolder(holder: Holder, i: Int) {
            holder.action.text = dataSet[i]
            holder.action.setOnClickListener { view -> listener.onItemClick(view, i) }
        }

        override fun getItemCount(): Int {
            return dataSet.size
        }
    }

    // List view holder
    internal class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.text)
        lateinit var action: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

    internal interface ItemClickListener {
        fun onItemClick(v: View?, position: Int)
    } // endregion
}
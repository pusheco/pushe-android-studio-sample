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
import co.pushe.plus.PusheUser
import co.pushe.plus.analytics.PusheAnalytics
import co.pushe.plus.inappmessaging.InAppMessage
import co.pushe.plus.inappmessaging.PusheInAppMessaging
import co.pushe.plus.inappmessaging.PusheInAppMessagingListener
import co.pushe.plus.notification.*
import co.pushe.sample.`as`.R
import co.pushe.sample.`as`.eventbus.MessageEvent
import co.pushe.sample.`as`.utils.Stuff
import co.pushe.sample.`as`.utils.Stuff.alert
import co.pushe.sample.`as`.utils.Stuff.prompt
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DateFormat
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
            Pushe.getPusheService(PusheNotification::class.java)
                ?.setNotificationListener(object : PusheNotificationListener {
                    override fun onNotification(notification: NotificationData) {
                        addText(
                            """
     Listener on receiving notification triggered
     ${notification.title}
     """.trimIndent()
                        )
                    }

                    override fun onCustomContentNotification(customContent: Map<String, Any>) {
                        addText("Listener on receiving notification with custom content triggered")
                    }

                    override fun onNotificationClick(notification: NotificationData) {
                        addText("Listener on clicking on notification triggered")
                    }

                    override fun onNotificationDismiss(notification: NotificationData) {
                        addText("Listener on dismissing notification triggered")
                    }

                    override fun onNotificationButtonClick(
                        button: NotificationButtonData,
                        notification: NotificationData
                    ) {
                        addText("Listener on clicking on notification button triggered")
                    }
                })
        }
        // Piam listener
        Pushe.getPusheService(PusheInAppMessaging::class.java)
            ?.setInAppMessagingListener(object : PusheInAppMessagingListener {
                override fun onInAppMessageReceived(inAppMessage: InAppMessage) {
                    addText(
                        """
           InAppMessage received:
           Title: ${inAppMessage.title}
           Content: ${inAppMessage.content}
           """.trimIndent()
                    )
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
        list.adapter = Adapter(menus())
    }

    private fun menus(): Map<String, () -> Unit> {
        return mapOf(
            "IDs" to {
                alert(
                    this@MainActivity, "IDs", """
     AndroidId:
     ${Pushe.getDeviceId()}
     GoogleAdId:
     ${Pushe.getAdvertisingId()}
     """.trimIndent()
                )
            },
            "Custom Id" to {
                prompt(this@MainActivity, "New custom id", "current custom id:" + Pushe.getCustomId(), Consumer { s ->
                    Pushe.setCustomId(s)
                    addText("Custom id is: " + Pushe.getCustomId())
                })
            },
            "Phone Number" to {
                prompt(
                    this@MainActivity,
                    "New phone number",
                    "current phone number is:" + Pushe.getUserPhoneNumber(),
                    Consumer { s ->
                        Pushe.setUserPhoneNumber(s)
                        addText("phone number is: " + Pushe.getUserPhoneNumber())
                    })
            },
            "Email" to {
                prompt(this@MainActivity, "New Email", "current email is:" + Pushe.getUserEmail(), Consumer { s ->
                    Pushe.setUserEmail(s)
                    addText("user email is: " + Pushe.getUserEmail())
                })
            },
            "Modules initialization status" to {
                addText("modules initialized: " + Pushe.isInitialized())
            },
            "Device registration status" to {
                addText("Device registered: " + Pushe.isRegistered())
            },
            "Topic" to {
                prompt(this@MainActivity, "Topic", """
     Topics:[${Pushe.getSubscribedTopics()}]
     Enter topic name to subscribe or unsubscribe
     """.trimIndent(), "topic name", "Subscribe", "Unsubscribe", object : Stuff.Callback<String> {
                    override fun onPositiveButtonClicked(t: String) {
                        Pushe.subscribeToTopic(t)
                        addText("Subscribe to topic: $t")
                    }

                    override fun onNegativeButtonClicked(t: String) {
                        Pushe.unsubscribeFromTopic(t)
                        addText("Unsubscribe from topic: $t")
                    }
                })
            },
            "Tag (name:value)" to {
                prompt(this@MainActivity, "Tags", """Tags:${Pushe.getSubscribedTags()}
Tag in name:value format (add)
 Tag in name1,name2 format (remove)""", "topic name", "Add", "Remove", object : Stuff.Callback<String> {
                    override fun onPositiveButtonClicked(t: String) {
                        if (!t.contains(":")) return
                        val keyValue = t.split(":").toTypedArray()
                        if (keyValue.size != 2) return
                        val map: MutableMap<String, String> = mutableMapOf()
                        map[keyValue[0]] = keyValue[1]
                        Pushe.addTags(map)
                        addText("Tag '" + keyValue[0] + "' added ")
                    }

                    override fun onNegativeButtonClicked(t: String) {
                        val keyList = t.split(",").toTypedArray()
                        if (keyList.isEmpty()) return
                        Pushe.removeTags(listOf(*keyList))
                        addText("Tags '$t' removed")
                    }
                })
            },
            "User: Report login" to {
                Pushe.userLoggedIn("log_data")
                addText("Calling user logIn")
            },
            "User: Report logout" to {
                Pushe.userLoggedOut()
                addText("Calling user logOut")
            },
            "User: Is loggedIn" to {
                addText("User is ${if (Pushe.isUserLoggedIn()) "" else "not "}logged in")
            },
            "User: Sync sample user attribute" to {
                val userToSync = Pushe.getCurrentUserAttributes() ?: PusheUser() // use this way to update current instead of creating a new one
                    .setBirth(12, 2, 1990)
                    .setCity("Sabzevar")
                    .setEmail("hi@pushe.co")
                    .setPostalCode("123445566")
                    .setLocality("tabas-square")
                    .setCompany("Pushe")
                    .setEmailOptedIn(true)
                    .setFirstName("Johnny")
                    .setLastName("Cage")
                Pushe.syncUserAttributes(userToSync)
                addText("Syncing user\n ${userToSync.toMap()}")
            },
            "User: Get current synced attribute" to {
                val user = Pushe.getCurrentUserAttributes()
                addText(user?.toMap()?.toString() ?: "No synced user is saved")
            },
            "Analytics: Event" to {
                prompt(this@MainActivity, "Event", "Type event name to send", Consumer { s ->
                    Pushe.getPusheService(PusheAnalytics::class.java)?.sendEvent(s)
                    addText("Sending event: $s")
                })
            },
            "Analytics: E-commerce" to {
                prompt(this@MainActivity, "E-commerce", "Enter value in name:price format to send data", Consumer { s ->
                    if (!s.contains(":")) return@Consumer
                    val keyValue = s.split(":").toTypedArray()
                    if (keyValue.size != 2) return@Consumer
                    try {
                        Pushe.getPusheService(PusheAnalytics::class.java)
                            ?.sendEcommerceData(keyValue[0], keyValue[1].toDouble())
                        addText(
                            "Sending E-commerce data with name " + keyValue[0] + " and price " + keyValue[1]
                        )
                    } catch (e: NumberFormatException) {
                        addText("Enter valid price (price should be double)")
                    }
                })
            },
            "Notification: AndroidId" to {
                prompt(
                    this@MainActivity,
                    "Notification",
                    "Enter androidId to send a notification to that user",
                    "androidId",
                    "Send to ...",
                    "Send to me",
                    object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withDeviceId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to AndroidId: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withDeviceId(Pushe.getDeviceId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to this device")
                        }
                    })
            },
            "Notification: GoogleAdId" to {
                prompt(
                    this@MainActivity,
                    "Notification",
                    "Enter GoogleAdId to send a notification to that user",
                    "GoogleAdId",
                    "Send to ...",
                    "Send to me",
                    object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withAdvertisementId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to GoogleAdId: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification =
                                UserNotification.withAdvertisementId(Pushe.getAdvertisingId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to this device")
                        }
                    })
            },
            "Notification: CustomId" to {
                prompt(
                    this@MainActivity,
                    "Notification",
                    "Enter custom id to send a notification to that user",
                    "custom id",
                    "Send to ...",
                    "Send to me",
                    object : Stuff.Callback<String> {
                        override fun onPositiveButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withCustomId(t)
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to custom id: $t")
                        }

                        override fun onNegativeButtonClicked(t: String) {
                            val userNotification: UserNotification = UserNotification.withCustomId(Pushe.getCustomId())
                            userNotification.setTitle("title1")
                            userNotification.setContent("content1")
                            Pushe.getPusheService(PusheNotification::class.java)
                                ?.sendNotificationToUser(userNotification)
                            addText("Sending notification to this device")
                        }
                    })
            },
            "Piam: Dismiss All InApps" to {
                inAppMessaging?.dismissShownInApp()
            },
            "Piam: Trigger event" to {

                prompt(this@MainActivity, "InAppMessaging", "Enter an event", "", Consumer { s ->
                    if (s.isEmpty() || listOf("%", "&", "@").contains(s) || s.length > 10) {
                        Toast.makeText(this@MainActivity, "Invalid event name", Toast.LENGTH_SHORT).show()
                    } else {
                        addText("Triggering event: $s")
                        inAppMessaging!!.triggerEvent(s)
                    }
                })
            }
        )
    }

    /**
     * Add text instead of replacing the text in android studio.
     * Takes a textView and a text and adds the text to the textView.
     */
    fun addText(text: String) {
        val textView = status
        val scrollView = scroll
        val currentText = textView.text as String
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        val newText = "$currentText\n-----\n$text\nTime: $currentDateTimeString\n"
        textView.text = newText
        scrollView.fullScroll(View.FOCUS_DOWN)
    }

    // region EventBus
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        addText(event.message)
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
    internal class Adapter(private val dataSet: Map<String, () -> Unit>) : RecyclerView.Adapter<Holder?>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): Holder {
            return Holder(LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, viewGroup, false))
        }

        override fun onBindViewHolder(holder: Holder, i: Int) {
            val key = dataSet.keys.elementAt(i)
            holder.action.text = key
            holder.action.setOnClickListener { view -> dataSet[key]?.invoke() }
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

    // endregion
}
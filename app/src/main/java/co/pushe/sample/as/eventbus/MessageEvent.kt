package co.pushe.sample.`as`.eventbus

/**
 * To send event through app from a place to another place, we use `EventBus`.
 * This class will be the object that will be sent and received.
 */
data class MessageEvent(val message: String)
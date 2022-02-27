package co.pushe.sample.`as`.utils

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Consumer
import co.pushe.sample.`as`.R
import java.text.DateFormat
import java.util.*

@Suppress("unused")
object Stuff {

    /**
     * Shows a dialog having an EditText.
     * @param callback will be called when user clicked on OK. Callback contains the text that was entered in editText.
     * @see Consumer which is a simple interface.
     */
    fun prompt(activityContext: Context, title: String, message: String, callback: Consumer<String>) {
        val editText = EditText(activityContext)
        editText.setSingleLine()
        editText.maxLines = 1
        AlertDialog.Builder(activityContext)
          .setIcon(R.drawable.ic_input_get)
          .setTitle(title)
          .setMessage(message)
          .setView(editText)
          .setPositiveButton("OK") { _, _ ->
              val text = editText.text.toString()
              callback.accept(text)
          }
          .setNegativeButton("Cancel") { _, _ ->
              //
          }.create().show()
    }

    /**
     * Shows a dialog having an EditText.
     * @param callback will be called when user clicked on button1(positive) or button2(negative). Callback contains the text that was entered in editText.
     * @see Callback which is a simple interface.
     */
    fun prompt(activityContext: Context, title: String, message: String, hint: String, button1: String, button2: String, callback: Callback<String>) {
        val editText = EditText(activityContext)
        editText.setSingleLine()
        editText.maxLines = 1
        editText.hint = hint
        AlertDialog.Builder(activityContext)
          .setIcon(R.drawable.ic_input_get)
          .setTitle(title)
          .setMessage(message)
          .setView(editText)
          .setPositiveButton(button1) { _, _ ->
              val text = editText.text.toString()
              callback.onPositiveButtonClicked(text)
          }
          .setNegativeButton(button2) { _, _ ->
              val text = editText.text.toString()
              callback.onNegativeButtonClicked(text)
          }.create().show()
    }

    /**
     * Same as the other function but can pass default text to editText.
     */
    @JvmStatic
    fun prompt(activityContext: Context, title: String, message: String, defaultText: String, callback: Consumer<String>) {
        val editText = EditText(activityContext)
        editText.setSingleLine()
        editText.maxLines = 1
        editText.setText(defaultText)
        AlertDialog.Builder(activityContext)
          .setIcon(R.drawable.ic_input_get)
          .setTitle(title)
          .setMessage(message)
          .setView(editText)
          .setPositiveButton("OK") { _, _ ->
              val text = editText.text.toString()
              callback.accept(text)
          }
          .setNegativeButton("Cancel", null).create().show()
    }

    /**
     * Show simple information using alertDialog.
     */
    @JvmStatic
    fun alert(activityContext: Context, title: String, message: String) {
        AlertDialog.Builder(activityContext)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton("OK", null)
          .create()
          .show()
    }


    interface Callback<T> {
        fun onPositiveButtonClicked(t: T)
        fun onNegativeButtonClicked(t: T)
    }
}
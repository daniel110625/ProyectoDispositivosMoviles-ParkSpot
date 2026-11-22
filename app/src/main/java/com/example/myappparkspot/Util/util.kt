
package com.example.myappparkspot.Util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent

object Util {

    fun isValidText(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    fun isValidPrice(price: Double): Boolean {
        return price >= 0
    }

    fun isValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return latitude in -90.0..90.0 &&
                longitude in -180.0..180.0
    }

    fun openActivity(context: Context, activity: Class<*>) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }

    fun showDialogCondition(
        context: Context,
        questionText: String,
        callback: () -> Unit
    ) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(questionText)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> callback() }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmation")
        alert.show()
    }
}

package com.example.myappparkspot

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.LinearLayout
import android.content.Intent
import android.widget.Button
import android.widget.PopupMenu

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnMenu = findViewById<Button>(R.id.btnMenu)

        btnMenu.setOnClickListener {

            val popupMenu = PopupMenu(this, btnMenu)

            popupMenu.menu.add("Register Parking")
            popupMenu.menu.add("Edit Parking")

            popupMenu.setOnMenuItemClickListener { item ->

                when (item.title.toString()) {

                    "Register Parking" -> {
                        val intent = Intent(this, ParkingFormActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    "Edit Parking" -> {
                        val intent = Intent(this, ParkingEditActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }
}
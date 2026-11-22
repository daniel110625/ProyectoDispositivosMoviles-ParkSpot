
package com.example.myappparkspot

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myappparkspot.Util.Util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnMenu: Button = findViewById<Button>(R.id.btnMenu)

        btnMenu.setOnClickListener(View.OnClickListener { view ->
            Util.openActivity(
                this,
                ParkingFormActivity::class.java
            )
        })

        val btnEditParking: Button = findViewById(R.id.btnEditParking)

        btnEditParking.setOnClickListener {
            Util.openActivity(
                this,
                ParkingEditActivity::class.java
            )
        }

    }
}

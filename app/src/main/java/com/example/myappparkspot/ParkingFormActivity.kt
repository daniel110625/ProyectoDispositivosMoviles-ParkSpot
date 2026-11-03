package com.example.myappparkspot

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.model.ParkingType
import com.example.myappparkspot.domain.model.VehicleType
import com.example.myappparkspot.viewmodel.ParkingViewModel

class ParkingFormActivity : AppCompatActivity() {

    private lateinit var txtParkingName: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtPrice: EditText
    private lateinit var txtOpeningHours: EditText
    private lateinit var btnSave: LinearLayout

    private var selectedVehicleType = VehicleType.CAR
    private var selectedParkingType = ParkingType.FREE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_form)

        initializeComponents()
        initializeEvents()
    }

    private fun initializeComponents() {
        txtParkingName = findViewById(R.id.txtParkingName)
        txtAddress = findViewById(R.id.txtAddress)
        txtPrice = findViewById(R.id.txtPrice)
        txtOpeningHours = findViewById(R.id.txtOpeningHours)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun initializeEvents() {

        findViewById<LinearLayout>(R.id.btnCar).setOnClickListener {
            selectedVehicleType = VehicleType.CAR
        }

        findViewById<LinearLayout>(R.id.btnMotorcycle).setOnClickListener {
            selectedVehicleType = VehicleType.MOTORCYCLE
        }

        findViewById<LinearLayout>(R.id.btnBoth).setOnClickListener {
            selectedVehicleType = VehicleType.BOTH
        }

        findViewById<LinearLayout>(R.id.btnFree).setOnClickListener {
            selectedParkingType = ParkingType.FREE
            txtPrice.setText("0")
        }

        findViewById<LinearLayout>(R.id.btnPaid).setOnClickListener {
            selectedParkingType = ParkingType.PAID
        }

        btnSave.setOnClickListener {
            saveParking()
        }
    }

    private fun saveParking() {

        val name = txtParkingName.text.toString()
        val address = txtAddress.text.toString()
        val priceText = txtPrice.text.toString()
        val openingHours = txtOpeningHours.text.toString()

        if (name.isEmpty() || address.isEmpty() || openingHours.isEmpty()) {
            Toast.makeText(
                this,
                "Please complete all required fields",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val price = if (priceText.isEmpty()) {
            0.0
        } else {
            priceText.toDouble()
        }

        val parking = Parking(
            id = ParkingViewModel.getParkings().size + 1,
            name = name,
            address = address,
            pricePerHour = price,
            vehicleType = selectedVehicleType,
            parkingType = selectedParkingType,
            openingHours = openingHours,
            imageUri = null,
            latitude = 0.0,
            longitude = 0.0
        )

        try {

            ParkingViewModel.createParking(parking)

            Toast.makeText(
                this,
                "Parking saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            clearFields()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun clearFields() {
        txtParkingName.text.clear()
        txtAddress.text.clear()
        txtPrice.text.clear()
        txtOpeningHours.text.clear()

        selectedVehicleType = VehicleType.CAR
        selectedParkingType = ParkingType.FREE
    }
}
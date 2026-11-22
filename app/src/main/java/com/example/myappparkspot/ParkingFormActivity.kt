
package com.example.myappparkspot

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myappparkspot.data.memory.ParkingRepositoryProvider
import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.model.ParkingType
import com.example.myappparkspot.domain.model.VehicleType
import com.example.myappparkspot.viewmodel.ParkingViewModel

class ParkingFormActivity : AppCompatActivity() {

    private lateinit var parkingViewModel: ParkingViewModel

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

        parkingViewModel = ParkingViewModel(
            ParkingRepositoryProvider.repository
        )

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

        // Back button
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        // Vehicle type
        findViewById<LinearLayout>(R.id.btnCar).setOnClickListener {
            selectedVehicleType = VehicleType.CAR
        }

        findViewById<LinearLayout>(R.id.btnMotorcycle).setOnClickListener {
            selectedVehicleType = VehicleType.MOTORCYCLE
        }

        findViewById<LinearLayout>(R.id.btnBoth).setOnClickListener {
            selectedVehicleType = VehicleType.BOTH
        }

        // Parking type
        findViewById<LinearLayout>(R.id.btnFree).setOnClickListener {
            selectedParkingType = ParkingType.FREE
            txtPrice.setText("0")
        }

        findViewById<LinearLayout>(R.id.btnPaid).setOnClickListener {
            selectedParkingType = ParkingType.PAID
        }

        // Save button
        btnSave.setOnClickListener {
            saveParking()
        }
    }

    private fun saveParking() {

        val name = txtParkingName.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val openingHours = txtOpeningHours.text.toString().trim()
        val priceText = txtPrice.text.toString().trim()

        if (name.isEmpty() || address.isEmpty() || openingHours.isEmpty()) {
            Toast.makeText(
                this,
                "Please complete all required fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val price = if (selectedParkingType == ParkingType.FREE) {
            0.0
        } else {

            val enteredPrice = priceText.toDoubleOrNull()

            if (enteredPrice == null || enteredPrice <= 0) {
                Toast.makeText(
                    this,
                    "Please enter a valid price",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            enteredPrice
        }

        val nextId = (parkingViewModel.getParkings().maxOfOrNull {
            it.id
        } ?: 0) + 1

        val parking = Parking(
            id = nextId,
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

            parkingViewModel.createParking(parking)

            val totalParkings = parkingViewModel.getParkings().size

            Toast.makeText(
                this,
                "Parking saved successfully. ID: ${parking.id}. Total: $totalParkings",
                Toast.LENGTH_LONG
            ).show()

            clearFields()

            // Return to MainActivity after saving
            finish()

        } catch (e: Exception) {

            Toast.makeText(
                this,
                e.message ?: "Error saving parking",
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

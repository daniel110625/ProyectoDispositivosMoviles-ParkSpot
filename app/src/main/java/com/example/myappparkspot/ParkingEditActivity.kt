package com.example.myappparkspot

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.model.ParkingType
import com.example.myappparkspot.domain.model.VehicleType
import com.example.myappparkspot.viewmodel.ParkingViewModel

class ParkingEditActivity : AppCompatActivity() {

    private lateinit var txtParkingId: EditText
    private lateinit var txtParkingName: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtPrice: EditText
    private lateinit var txtOpeningHours: EditText

    private lateinit var btnSearch: Button
    private lateinit var btnUpdate: LinearLayout

    private var selectedParking: Parking? = null
    private var isEditMode: Boolean = false

    private var selectedVehicleType = VehicleType.CAR
    private var selectedParkingType = ParkingType.FREE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_edit)

        initializeComponents()
        initializeEvents()
    }

    private fun initializeComponents() {

        txtParkingId = findViewById(R.id.txtParkingId)
        txtParkingName = findViewById(R.id.txtParkingName)
        txtAddress = findViewById(R.id.txtAddress)
        txtPrice = findViewById(R.id.txtPrice)
        txtOpeningHours = findViewById(R.id.txtOpeningHours)

        btnSearch = findViewById(R.id.btnSearch)
        btnUpdate = findViewById(R.id.btnUpdate)

        btnUpdate.isEnabled = false
    }

    private fun initializeEvents() {

        btnSearch.setOnClickListener {
            searchParking()
        }

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
    }

    private fun searchParking() {

        isEditMode = false
        selectedParking = null
        btnUpdate.isEnabled = false

        txtParkingName.text.clear()
        txtAddress.text.clear()
        txtPrice.text.clear()
        txtOpeningHours.text.clear()

        val idText = txtParkingId.text.toString()

        if (idText.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter a parking ID",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val id = idText.toIntOrNull()

        if (id == null || id <= 0) {
            Toast.makeText(
                this,
                "Invalid parking ID",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val parking = ParkingViewModel.getParkingById(id)

        if (parking != null) {

            selectedParking = parking
            isEditMode = true

            txtParkingName.setText(parking.name)
            txtAddress.setText(parking.address)
            txtPrice.setText(parking.pricePerHour.toString())
            txtOpeningHours.setText(parking.openingHours)

            selectedVehicleType = parking.vehicleType
            selectedParkingType = parking.parkingType

            btnUpdate.isEnabled = true

            Toast.makeText(
                this,
                "Parking found successfully",
                Toast.LENGTH_SHORT
            ).show()

        } else {

            Toast.makeText(
                this,
                "Parking not found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

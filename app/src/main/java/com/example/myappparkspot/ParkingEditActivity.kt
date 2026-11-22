
package com.example.myappparkspot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myappparkspot.Util.Util
import com.example.myappparkspot.data.memory.ParkingRepositoryProvider
import com.example.myappparkspot.domain.model.Parking
import com.example.myappparkspot.domain.model.ParkingType
import com.example.myappparkspot.domain.model.VehicleType
import com.example.myappparkspot.viewmodel.ParkingViewModel

class ParkingEditActivity : AppCompatActivity() {

    private lateinit var parkingViewModel: ParkingViewModel

    private lateinit var txtParkingId: EditText
    private lateinit var txtParkingName: EditText
    private lateinit var txtAddress: EditText
    private lateinit var txtPrice: EditText
    private lateinit var txtOpeningHours: EditText

    private var selectedVehicleType = VehicleType.CAR
    private var selectedParkingType = ParkingType.FREE
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking_edit)

        parkingViewModel = ParkingViewModel(
            ParkingRepositoryProvider.repository
        )

        initializeComponents()
        initializeEvents()
    }

    private fun initializeComponents() {
        txtParkingId = findViewById(R.id.txtParkingId)
        txtParkingName = findViewById(R.id.txtParkingName)
        txtAddress = findViewById(R.id.txtAddress)
        txtPrice = findViewById(R.id.txtPrice)
        txtOpeningHours = findViewById(R.id.txtOpeningHours)
    }

    private fun initializeEvents() {

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnUpdate = findViewById<LinearLayout>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnBack.setOnClickListener {
            finish()
        }

        btnSearch.setOnClickListener {
            searchParking(txtParkingId.text.toString().trim())
        }

        findViewById<LinearLayout>(R.id.btnCar).setOnClickListener {
            selectedVehicleType = VehicleType.CAR
            updateSelections()
        }

        findViewById<LinearLayout>(R.id.btnMotorcycle).setOnClickListener {
            selectedVehicleType = VehicleType.MOTORCYCLE
            updateSelections()
        }

        findViewById<LinearLayout>(R.id.btnBoth).setOnClickListener {
            selectedVehicleType = VehicleType.BOTH
            updateSelections()
        }

        findViewById<LinearLayout>(R.id.btnFree).setOnClickListener {
            selectedParkingType = ParkingType.FREE
            txtPrice.setText("0")
            updateSelections()
        }

        findViewById<LinearLayout>(R.id.btnPaid).setOnClickListener {
            selectedParkingType = ParkingType.PAID
            updateSelections()
        }

        btnUpdate.setOnClickListener {
            if (isEditMode) {
                Util.showDialogCondition(
                    this,
                    "Do you want to update this parking?"
                ) {
                    updateParking()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please search for a parking first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        btnDelete.setOnClickListener {
            if (isEditMode) {
                Util.showDialogCondition(
                    this,
                    "Do you want to delete this parking?"
                ) {
                    deleteParking()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please search for a parking first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun searchParking(id: String) {

        if (id.isBlank()) {
            Toast.makeText(
                this,
                "Please enter a parking ID",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val parkingId = id.toIntOrNull()

        if (parkingId == null) {
            Toast.makeText(
                this,
                "Invalid parking ID",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val parking = parkingViewModel.getParkingById(parkingId)

        if (parking != null) {
            isEditMode = true
            txtParkingId.isEnabled = false

            txtParkingName.setText(parking.name)
            txtAddress.setText(parking.address)
            txtPrice.setText(parking.pricePerHour.toString())
            txtOpeningHours.setText(parking.openingHours)

            selectedVehicleType = parking.vehicleType
            selectedParkingType = parking.parkingType
            updateSelections()

            Toast.makeText(
                this,
                "Parking found successfully",
                Toast.LENGTH_LONG
            ).show()

        } else {
            isEditMode = false
            Toast.makeText(
                this,
                "Parking not found",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun updateParking() {

        val id = txtParkingId.text.toString().toIntOrNull()
        if (!isEditMode || id == null) return

        val name = txtParkingName.text.toString().trim()
        val address = txtAddress.text.toString().trim()
        val openingHours = txtOpeningHours.text.toString().trim()

        if (!Util.isValidText(name) ||
            !Util.isValidText(address) ||
            !Util.isValidText(openingHours)
        ) {
            Toast.makeText(
                this,
                "Please complete all required fields",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val price = if (selectedParkingType == ParkingType.FREE) {
            0.0
        } else {
            val enteredPrice = txtPrice.text.toString().toDoubleOrNull()

            if (enteredPrice == null || enteredPrice <= 0) {
                Toast.makeText(
                    this,
                    "Please enter a valid price",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            enteredPrice
        }

        val existingParking = parkingViewModel.getParkingById(id)

        if (existingParking == null) {
            Toast.makeText(
                this,
                "Parking not found",
                Toast.LENGTH_LONG
            ).show()
            cleanScreen()
            return
        }

        val parking = Parking(
            id = id,
            name = name,
            address = address,
            pricePerHour = price,
            vehicleType = selectedVehicleType,
            parkingType = selectedParkingType,
            openingHours = openingHours,
            imageUri = existingParking.imageUri,
            latitude = existingParking.latitude,
            longitude = existingParking.longitude
        )

        try {
            parkingViewModel.updateParking(parking)

            Toast.makeText(
                this,
                "Parking updated successfully",
                Toast.LENGTH_LONG
            ).show()

            cleanScreen()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message ?: "Error updating parking",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteParking() {

        val id = txtParkingId.text.toString().toIntOrNull()
        if (!isEditMode || id == null) return

        try {
            parkingViewModel.removeParking(id)

            Toast.makeText(
                this,
                "Parking deleted successfully",
                Toast.LENGTH_LONG
            ).show()

            cleanScreen()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                e.message ?: "Error deleting parking",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun cleanScreen() {

        isEditMode = false

        txtParkingId.isEnabled = true
        txtParkingId.setText("")
        txtParkingName.setText("")
        txtAddress.setText("")
        txtPrice.setText("")
        txtOpeningHours.setText("")

        selectedVehicleType = VehicleType.CAR
        selectedParkingType = ParkingType.FREE

        updateSelections()
    }

    private fun updateSelections() {

        findViewById<LinearLayout>(R.id.btnCar).setBackgroundResource(
            if (selectedVehicleType == VehicleType.CAR)
                R.drawable.bg_vehicle_selected
            else R.drawable.bg_vehicle_normal
        )

        findViewById<LinearLayout>(R.id.btnMotorcycle).setBackgroundResource(
            if (selectedVehicleType == VehicleType.MOTORCYCLE)
                R.drawable.bg_vehicle_selected
            else R.drawable.bg_vehicle_normal
        )

        findViewById<LinearLayout>(R.id.btnBoth).setBackgroundResource(
            if (selectedVehicleType == VehicleType.BOTH)
                R.drawable.bg_vehicle_selected
            else R.drawable.bg_vehicle_normal
        )

        findViewById<LinearLayout>(R.id.btnFree).setBackgroundResource(
            if (selectedParkingType == ParkingType.FREE)
                R.drawable.bg_free_option
            else R.drawable.bg_paid_option
        )

        findViewById<LinearLayout>(R.id.btnPaid).setBackgroundResource(
            R.drawable.bg_paid_option
        )
    }
}

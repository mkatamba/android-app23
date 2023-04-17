package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.adapter.AppointmentAdapter
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityAppointmentBinding

class AppointmentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout and set it as the content view
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set click listener for the floating action button to show the bottom sheet
        binding.floatingActionButton.setOnClickListener {
            val modalBottomSheet = AppointmentBottomSheet(null)
            modalBottomSheet.show(supportFragmentManager, AppointmentBottomSheet.TAG)


        }
        binding.run {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()         // Set click listener for the top app bar navigation icon to finish the activity

            }
            // Query for appointments and set up the recycler view adapter
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "appointment").addSnapshotListener { value, error ->
                    if (error != null) {  
                        // Show an error message dialog if there was an error retrieving the data
                        MaterialAlertDialogBuilder(this@AppointmentActivity).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    }
                     // Parse the appointment data from the snapshots and add it to the list
                    var list = arrayListOf<com.miniaimer.domain.modal.Reminder>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var content = it.getString("content")
                        var time = it.getLong("timed")
                        var obj =
                            com.miniaimer.domain.modal.Reminder(it.id, time!!, title!!, content!!)
                        list.add(obj)
                    }
                    // Set up the recycler view adapter with the appointment list and the support fragment manager
                    val llm = LinearLayoutManager(this@AppointmentActivity)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    listAppointment.setLayoutManager(llm)
                    listAppointment.adapter = AppointmentAdapter(list, supportFragmentManager)

                }

        }
    }
}
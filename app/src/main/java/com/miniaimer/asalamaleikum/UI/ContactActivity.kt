package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.adapter.AppointmentAdapter
import com.miniaimer.asalamaleikum.adapter.ContactAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {  // Inflate the layout using the binding
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
         // Set up the FAB to show the bottom sheet when clicked
        binding.run {  // Access views from the binding
            floatingActionButton.setOnClickListener {
                val modalBottomSheet = ContactBottomSheet(null)
                modalBottomSheet.show(supportFragmentManager, ContactBottomSheet.TAG)

            }
            // Set up the back button on the top app bar
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            // Listen for changes to the "contact" tasks in Firestore    
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "contact").addSnapshotListener { value, error ->
                    if (error != null) {  // Show no error message if there is no error retrieving data
                        MaterialAlertDialogBuilder(this@ContactActivity).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->
                        // Do nothing when "Cancel" button is clicked
                            }.setCancelable(false).show()
                    }
                    // Parse the documents and add them to the list of contacts
                    var list = arrayListOf<com.miniaimer.domain.modal.Reminder>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var content = it.getString("content")
                        var time = it.getLong("time")
                        var obj =
                            com.miniaimer.domain.modal.Reminder(it.id, time!!, title!!, content!!)
                        list.add(obj)
                    }
                    // Set up the RecyclerView to display the list of contacts
                    val llm = LinearLayoutManager(this@ContactActivity)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    listContact.setLayoutManager(llm)
                    listContact.adapter = ContactAdapter(list, supportFragmentManager)

                }

        }
    }
}
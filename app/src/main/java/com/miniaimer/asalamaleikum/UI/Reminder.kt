package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.adapter.BankCardAdapter
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityReminderBinding
// Declaring Reminder class, inheriting from AppCompatActivity class
class Reminder : AppCompatActivity() {
    // Binding instance variable for the view
    lateinit var binding: ActivityReminderBinding
    // onCreate() method, which is called when the activity is starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the view using layoutInflater
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // OnClick Listener for the Floating Action Button to show bottom sheet
        binding.floatingActionButton.setOnClickListener {
            val modalBottomSheet = ReminderBottomSheet(null)

            modalBottomSheet.show(supportFragmentManager, ReminderBottomSheet.TAG)


        }
        // Accessing task collection of current user from firebase firestore and getting the snapshot listener
        binding.run {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "reminder").addSnapshotListener { value, error ->
                    // Show error if any error occurs during snapshot listening
                    if (error != null) {
                        MaterialAlertDialogBuilder(this@Reminder).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    }
                    // Storing data from firebase firestore into an array list and passing it to ReminderAdapter
                    var list = arrayListOf<com.miniaimer.domain.modal.Reminder>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var content = it.getString("content")
                        var time = it.getLong("timed")
                        var obj =
                            com.miniaimer.domain.modal.Reminder(it.id, time!!, title!!, content!!)
                        list.add(obj)
                    }
                    // Setting up ReminderAdapter with recycler view
                    val llm = LinearLayoutManager(this@Reminder)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    listReminder.setLayoutManager(llm)
                    listReminder.adapter = ReminderAdapter(list, supportFragmentManager)

                }

        }
    }
}
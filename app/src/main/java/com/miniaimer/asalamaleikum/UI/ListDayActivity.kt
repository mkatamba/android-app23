package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.miniaimer.asalamaleikum.adapter.ListDayAdapter
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityListDayBinding
import com.miniaimer.domain.modal.Day

class ListDayActivity : AppCompatActivity() {
    lateinit var binding: ActivityListDayBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          // Inflate the layout and set it as the content view
        binding = ActivityListDayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve the extras from the intent
        val extras = intent.extras
        // Initialize day to 0
        var day = 0
        // Set navigation click listener to handle back button press
        binding.topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
         // Check if extras is not null
        if (extras != null) {
              // Retrieve the day value from the extras and convert it
            day = extras.getString("day").toString().toInt()
            // Log the difference between the current time and the day value
            Log.e("loimẹ", (System.currentTimeMillis() - day).toString())
            // Check if the day value is not equal to zero
            if (day != 0) {
                  // Check if the day value is equal to 86400000 (one day)
                if (day == 86400000) {
                       // Set the title of the top app bar to "My Day"
                    binding.topAppBar.title = "My Day"
                } else {
                     // Set the title of the top app bar to "7 Days"
                    binding.topAppBar.title = "7 Days"
                }
                 // Query the Firebase Firestore for tasks with a time greater than the difference between
                // the current time and the day value, and order them by time in descending order
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .whereGreaterThan("time", System.currentTimeMillis() - day)
                    .orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener {
                        // Create a list of Day objects from the documents returned in the query
                        var list = arrayListOf<Day>()
                        it.result.documents.forEach {
                            var title = it.getString("title")
                            var content = it.getString("content")
                            var type = it.getString("type")
                            var time = it.getLong("time")
                            var id = it.id
                            var d = Day(id, type, title, content, time)
                            list.add(d)
                        }
                         // Create a linear layout manager for the RecyclerView and set it to vertical orientation
                        val llm = LinearLayoutManager(this@ListDayActivity)
                        llm.setOrientation(LinearLayoutManager.VERTICAL)
                        // Set the RecyclerView adapter to a new instance of the ListDayAdapter with the list of Day objects
                        binding.listday.setLayoutManager(llm)
                        binding.listday.adapter = ListDayAdapter(list, supportFragmentManager)
                    }
            } else {
                // Set the title of the top app bar to "All Tasks"
                binding.topAppBar.title = "All Tasks"
                  // Query the Firebase Firestore for all tasks and order them by time in descending order
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener {
                        var list = arrayListOf<Day>()
                        it.result.documents.forEach {
                            var title = it.getString("title")
                            var content = it.getString("content")
                            var type = it.getString("type")
                            var time = it.getLong("time")
                            var id = it.id
                            var d = Day(id, type, title, content, time)
                            list.add(d)
                        }
                        val llm = LinearLayoutManager(this@ListDayActivity)
                        llm.setOrientation(LinearLayoutManager.VERTICAL)
                        binding.listday.setLayoutManager(llm)
                        binding.listday.adapter = ListDayAdapter(list, supportFragmentManager)
                    }
            }

        }
    }
}
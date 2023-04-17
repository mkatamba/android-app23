package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.Query
import com.miniaimer.asalamaleikum.adapter.NoteAdapter
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityNoteBinding
import com.miniaimer.domain.modal.Note
// Define the main activity class
class NoteActivity : AppCompatActivity() {
    // Declare class level variables
    lateinit var binding: ActivityNoteBinding
    lateinit var loading: LoadingDialogFragment
    // Define the onCreate method which gets called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the view using the binding
        binding = ActivityNoteBinding.inflate(layoutInflater)
        // Set the content view to the inflated view
        setContentView(binding.root)
        // Set click listener for the floating action button
        binding.run {
            floatingActionButton.setOnClickListener {
                startActivity(Intent(this@NoteActivity, editNote::class.java))
            }
            // Set click listener for the top app bar navigation icon
            topAppBar.setNavigationOnClickListener { onBackPressed() }
             // Fetch all the notes for the current user from the database
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "note").orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                // Show an error dialog if there's an error
                    if (error != null) {
                        MaterialAlertDialogBuilder(this@NoteActivity).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    }
                     // Iterate through all the documents and create a Note object for each document
                    var list = arrayListOf<Note>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var content = it.getString("content")
                        var obj = Note(it.id, title!!, content!!)
                        list.add(obj)
                    }
                    // Create a new LinearLayoutManager and set it to the RecyclerView
                    val llm = LinearLayoutManager(this@NoteActivity)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    listnote.setLayoutManager(llm)
                    // Set the adapter for the RecyclerView
                    listnote.adapter = NoteAdapter(list)

                }

        }
    }
}
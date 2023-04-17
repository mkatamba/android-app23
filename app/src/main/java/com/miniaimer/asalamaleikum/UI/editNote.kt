package com.miniaimer.asalamaleikum.UI

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityEditNoteBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class editNote : AppCompatActivity() {
    lateinit var binding: ActivityEditNoteBinding
    // Declare variable id
    var id = ""
    // Declare loading dialog fragment
    lateinit var loading: LoadingDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         // Inflate activity layout
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Show loading dialog fragment
        loading = LoadingDialogFragment()
        loading.show(supportFragmentManager, "l")
        // Get extras from intent
        val extras = intent.extras
        if (extras != null) {
            // Get id from extras
            id = extras.getString("id").toString()
            Log.e("idzxzx", id)  // Check if id length is greater than 5
            if (id.length > 5) {
                // Get document from Firebase Firestore
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .document(id!!).get().addOnCompleteListener {
                        if (it.isComplete) {
                             // Set title and content fields with data from document
                            binding.title.setText(it.result.getString("title"))
                            binding.content.setText(it.result.getString("content"))
                            // Dismiss loading dialog fragment if it is visible
                            if (loading.isVisible) {
                                loading.dismiss()
                            }

                        } else {
                            // Show error message
                            if (loading.isVisible) {
                                loading.dismiss()
                            }
                        }
                    }
            } else {
                lifecycleScope.launch {
                    // Delay for 500 milliseconds
                    delay(500)
                    // Dismiss loading dialog fragment if it is still visible
                    if (loading.isVisible) {
                        loading.dismiss()

                    }
                }
            }

        } else {
            lifecycleScope.launch {
                delay(500)
                if (loading.isVisible) {
                    loading.dismiss()

                }
            }

        }
        binding.run {

            topAppBar.setNavigationOnClickListener {

                onBackPressed()

            }
        }
    }

    override fun onBackPressed() {

        try {
            var title = binding.title.text?.toString()?.trim()
            var content = binding.content.text?.toString()?.trim()
            loading.show(supportFragmentManager, "l")
            if (content!!.length > 0 || title!!.length > 0) {
                if (id != "" || id.length > 0) {
                    App.db.collection("users").document(App.auth.currentUser!!.uid)
                        .collection("task").document(id).update("title", title)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                App.db.collection("users").document(App.auth.currentUser!!.uid)
                                    .collection("task").document(id).update("content", content)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            App.db.collection("users")
                                                .document(App.auth.currentUser!!.uid)
                                                .collection("task").document(id)
                                                .update("time", System.currentTimeMillis())
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        if (loading.isVisible) {
                                                            loading.dismiss()
                                                        }
                                                        super.onBackPressed()
                                                        Toast.makeText(
                                                            this@editNote,
                                                            "Save Success",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } else {
                                                        if (loading.isVisible) {
                                                            loading.dismiss()
                                                        }
                                                        super.onBackPressed()
                                                        Toast.makeText(
                                                            this@editNote,
                                                            it.exception?.localizedMessage,
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                        } else {
                                            if (loading.isVisible) {
                                                loading.dismiss()
                                            }
                                            super.onBackPressed()
                                            Toast.makeText(
                                                this@editNote,
                                                it.exception?.localizedMessage,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                if (loading.isVisible) {
                                    loading.dismiss()
                                }
                                super.onBackPressed()
                                Toast.makeText(
                                    this@editNote,
                                    it.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                } else {
                    val note = hashMapOf(
                        "title" to title,
                        "content" to content,
                        "type" to "note",
                        "time" to System.currentTimeMillis()
                    )
                    App.db.collection("users").document(App.auth.currentUser!!.uid)
                        .collection("task").add(note).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this@editNote, "Save Success", Toast.LENGTH_SHORT)
                                    .show()
                                if (loading.isVisible) {
                                    loading.dismiss()
                                }
                                super.onBackPressed()
                            } else {

                                Toast.makeText(
                                    this@editNote,
                                    it.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (loading.isVisible) {
                                    loading.dismiss()
                                }
                                super.onBackPressed()
                            }
                        }
                }

            } else {
                super.onBackPressed()
            }

        } catch (e: Exception) {
        }


    }

    override fun onStop() {
        super.onStop()

    }
}
package com.miniaimer.asalamaleikum.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.firestore.SetOptions
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.app.App

class ContactBottomSheet(val id: String?) : BottomSheetDialogFragment() {
    // Inflate the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.contact_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // Get the BottomSheetBehavior for the bottom sheet view
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.standard_bottom_sheet))
        standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL    // saving bottom sheet state
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED   // expanding bottom sheet state
        // Initialize variables and views
        var isUpdate = false
        var add = view.findViewById<Button>(R.id.add)
        var addtitle = view.findViewById<TextInputLayout>(R.id.addtitle)
        var addcontent = view.findViewById<TextInputLayout>(R.id.addcontent)

        id?.let {
            if (id != "" || id != null) {  // checking if id is null or empty
                isUpdate = true
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .document(id).get().addOnCompleteListener {
                        if (it.isComplete) {
                            addtitle.editText?.setText(it.result.getString("title"))
                            addcontent.editText?.setText(it.result.getString("content"))
                        }
                    }
            } else {
                isUpdate = false
            }
        }// Set click listener for the add button
        add.setOnClickListener {
            if (addtitle.editText?.text.toString().trim().length == 0) {  // checking if title is empty
                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                    .setMessage("Can not leave the title blank")
                    .setNegativeButton("Cancel") { dialog, which ->

                    }.setCancelable(false).show()
            } else {
                if (addcontent.editText?.text.toString().trim().length == 0) {    // checking if content is empty
                    MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                        .setMessage("Content cannot be blank")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                } else {
                    // Create a reminder object
                    val reminder = hashMapOf(
                        "title" to addtitle.editText?.text.toString().trim(),
                        "content" to addcontent.editText?.text.toString().trim(),
                        "time" to System.currentTimeMillis(),
                        "type" to "contact"
                    )
                    // Check if the fragment is in update mode
                    if (!isUpdate) {
                        // Add the reminder to the database
                        App.db.collection("users").document(App.auth.currentUser!!.uid)
                            .collection("task").add(reminder).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    dismiss()
                                } else {
                                    MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                        .setMessage(it.exception?.localizedMessage)
                                        .setNegativeButton("Cancel") { dialog, which ->

                                        }.setCancelable(false).show()
                                }
                            }
                    } else {
                         // Update the existing reminder in the database
                        App.db.collection("users").document(App.auth.currentUser!!.uid)
                            .collection("task").document(id!!).set(reminder, SetOptions.merge())
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    dismiss()
                                } else {
                                    MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                        .setMessage(it.exception?.localizedMessage)
                                        .setNegativeButton("Cancel") { dialog, which ->

                                        }.setCancelable(false).show()
                                }
                            }

                    }
                }
            }
        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
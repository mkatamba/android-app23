package com.miniaimer.asalamaleikum.UI

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
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

class BankBottomSheet(val id: String?) : BottomSheetDialogFragment() {
    override fun onCreateView( // Inflate the layout for this fragment
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bank_sheet, container, false)// Inflate the layout for this fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // Get the bottom sheet behavior and expand it
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.standard_bottom_sheet))
        standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        // Inflate the layout for this fragment
        var setd = false
        var isUpdate = false
        var time = view.findViewById<Button>(R.id.setTime)
        var date = view.findViewById<Button>(R.id.setDate)
        var radio_button_1 = view.findViewById<RadioButton>(R.id.radio_button_1)
        var radio_button_2 = view.findViewById<RadioButton>(R.id.radio_button_2)
        var add = view.findViewById<Button>(R.id.add)
        var addtitle = view.findViewById<TextInputLayout>(R.id.addtitle)
        var addcontent = view.findViewById<TextInputLayout>(R.id.addcontent)
        // Set up date and time picker constraints
        val constraintsBuilder =
            CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())
            // Initialize time picker
        var picker = MaterialTimePicker.Builder().setInputMode(INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H).setHour(0).setMinute(0)
            .setTitleText("Select Reminder time").build()
             // Initialize date picker
        var datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build()).setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
              // Check if the ID is not null (i.e. update mode)
        id?.let {
            if (id != "" || id != null) {
                isUpdate = true
                // Get the task document with the specified ID from Firestore
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .document(id).get().addOnCompleteListener {
                        if (it.isComplete) {
                            setd = true
                             // Set the title and content EditTexts to the task's title and content
                            addtitle.editText?.setText(it.result.getString("title"))
                            addcontent.editText?.setText(it.result.getString("content"))
                            radio_button_1.isChecked = it.result.getBoolean("banktype")!!
                            radio_button_2.isChecked = !it.result.getBoolean("banktype")!!
                            // Initialize the time picker with the task's time!!
                            val dateTime = java.util.Date(it.result.getLong("timed")!!)
                            picker = MaterialTimePicker.Builder().setInputMode(INPUT_MODE_CLOCK)
                                .setTimeFormat(TimeFormat.CLOCK_24H).setHour(dateTime.hours)
                                .setMinute(dateTime.minutes).setTitleText("Select Reminder time")
                                .build()
                                   // Set the calendar picker to the task's date
                            val february = it.result.getLong("timed")
                            val constraintsBuilder = CalendarConstraints.Builder()
                                .setValidator(DateValidatorPointForward.now()).setOpenAt(february!!)
                            datePicker = MaterialDatePicker.Builder.datePicker()
                                .setCalendarConstraints(constraintsBuilder.build())
                                .setTitleText("Select date")
                                .setSelection(it.result.getLong("timed"))
                                .setCalendarConstraints(constraintsBuilder.build()).build()
                        }
                    }
            } else {
                isUpdate = false
            }
        }
        time.setOnClickListener {
            picker.show(childFragmentManager, "Ok")
        }
        date.setOnClickListener {
            datePicker.show(childFragmentManager, "s")
        }
        datePicker.addOnPositiveButtonClickListener {
            setd = true
        }
        add.setOnClickListener {


            datePicker.selection?.let {
                if (addtitle.editText?.text.toString().trim().length == 0) {
                    MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                        .setMessage("Can not leave the title blank")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                } else {
                    if (addcontent.editText?.text.toString().trim().length == 0) {
                        MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                            .setMessage("Content cannot be blank")
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    } else {
                        if (it == null || !setd) {
                            MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                .setMessage("Date cannot be blank")
                                .setNegativeButton("Cancel") { dialog, which ->

                                }.setCancelable(false).show()
                        } else {
                            var hourmili = 0L
                            var minutemili = 0L
                            try {
                                hourmili = picker.hour.times(3600000L)
                                minutemili = picker.minute.times(60000L)

                            } catch (e: Exception) {

                            }
                            var datemini = 0L
                            datemini =
                                datePicker.selection!! + hourmili!! + minutemili!! - 25200000L

                            val reminder = hashMapOf(
                                "title" to addtitle.editText?.text.toString().trim(),
                                "content" to addcontent.editText?.text.toString().trim(),
                                "timed" to datemini,
                                "time" to System.currentTimeMillis(),
                                "banktype" to radio_button_1.isChecked,
                                "type" to "bank"
                            )
                            if (!isUpdate) {
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
                                App.db.collection("users").document(App.auth.currentUser!!.uid)
                                    .collection("task").document(id!!)
                                    .set(reminder, SetOptions.merge()).addOnCompleteListener {
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
        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
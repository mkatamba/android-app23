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

class SocialBottomSheet(val id: String?) : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.social_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById(R.id.sosical_bottom_sheet))
        standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        var isUpdate = false
        var add = view.findViewById<Button>(R.id.add)
        var addtitle = view.findViewById<TextInputLayout>(R.id.addtitle)
        var addcontent = view.findViewById<TextInputLayout>(R.id.addcontent)
        var addpassword = view.findViewById<TextInputLayout>(R.id.addpassword)
        id?.let {
            if (id != "" || id != null) {
                isUpdate = true
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .document(id!!).get().addOnCompleteListener {
                        if (it.isComplete) {
                            addtitle.editText?.setText(it.result.getString("title"))
                            addcontent.editText?.setText(it.result.getString("content"))
                            addpassword.editText?.setText(it.result.getString("pass"))
                        } else {
                            MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                .setMessage(it.exception?.localizedMessage)
                                .setNegativeButton("Cancel") { dialog, which ->

                                }.setCancelable(false).show()
                        }
                    }

            } else {
                isUpdate = false
            }
        }

        add.setOnClickListener {
            var addt = addtitle.editText?.text
            var addc = addcontent.editText?.text
            var addp = addpassword.editText?.text
            if (addt!!.length > 0 || addc!!.length > 0 || addp!!.length > 0) {
                if (isUpdate) {
                    val social = mapOf(
                        "title" to addt.toString().trim(),
                        "content" to addc.toString().trim(),
                        "pass" to addp.toString().trim(),
                        "type" to "social",
                        "time" to System.currentTimeMillis()
                    )
                    App.db.collection("users").document(App.auth.currentUser!!.uid)
                        .collection("task").document(id!!).update(social).addOnCompleteListener {
                            if (it.isComplete) {
                                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                    .setMessage("Update success")
                                    .setNegativeButton("Cancel") { dialog, which ->

                                    }.setCancelable(false).show()
                            } else {
                                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                    .setMessage(it.exception?.localizedMessage)
                                    .setNegativeButton("Cancel") { dialog, which ->

                                    }.setCancelable(false).show()
                            }
                            dismiss()
                        }
                } else {
                    val social = hashMapOf(
                        "title" to addt.toString().trim(),
                        "content" to addc.toString().trim(),
                        "pass" to addp.toString().trim(),
                        "type" to "social",
                        "time" to System.currentTimeMillis()
                    )
                    App.db.collection("users").document(App.auth.currentUser!!.uid)
                        .collection("task").add(social).addOnCompleteListener {
                            if (it.isComplete) {
                                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                    .setMessage("Create success")
                                    .setNegativeButton("Cancel") { dialog, which ->

                                    }.setCancelable(false).show()
                            } else {
                                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                                    .setMessage(it.exception?.localizedMessage)
                                    .setNegativeButton("Cancel") { dialog, which ->

                                    }.setCancelable(false).show()
                            }
                            dismiss()
                        }
                }
            } else {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Notification")
                    .setMessage("Fields cannot be left blank")
                    .setNegativeButton("Cancel") { dialog, which ->

                    }.setCancelable(false).show()
            }

        }

    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
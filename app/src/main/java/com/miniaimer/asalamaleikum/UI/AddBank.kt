package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.SetOptions
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityAddBankBinding

class AddBank : AppCompatActivity() {
    lateinit var binding : ActivityAddBankBinding
    var time = 0L  // Variable to store the expiration date selected by the user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBankBinding.inflate(layoutInflater) // Inflates the layout using ViewBinding
        setContentView(binding.root)
        // Creates a date picker with validation rules
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        var datePicker =
            MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select expires").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        binding.run {// Shows the date picker when the expiration date field is clicked
            expires.setOnClickListener {
                datePicker.show(supportFragmentManager,"s")
            }  
            // Stores the selected expiration date in the time variable
            datePicker.addOnPositiveButtonClickListener {
                time = datePicker.selection!!
            }
             // Handles the back button in the top app bar
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            // Handles the "add" button click
            add.setOnClickListener {
                var loading = LoadingDialogFragment()
                loading.show(supportFragmentManager,"l")
                var cvv = cvv.editText?.text   // Extracts the information entered by the user
                var holdername = NameCard.editText?.text
                var bankname = bankname.editText?.text
                var banknumber = banknumber.editText?.text
                // Validates the information
                if (time > 0L && cvv!!.length > 0 && holdername!!.length >0 &&bankname!!.length >0 &&banknumber!!.length>15 ){
                    // Creates a hashmap with the information
                    val bank = hashMapOf(
                        "title" to "Bank",
                        "content" to "Add new Bank",
                        "timed" to time,
                        "time" to System.currentTimeMillis(),
                        "cvv" to cvv.toString(),
                        "holdername" to holdername.toString(),
                        "bankname" to bankname.toString(),
                        "banknumber" to banknumber.toString(),
                        "type" to "banknew"
                    )
                    // Sends the hashmap to the database
            // (the code to send the data to the database is not shown here)
                    App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").add(bank).addOnCompleteListener {
                        if (it.isSuccessful){
                            loading.dismiss()
                            finish()
                        }else{

                            loading.dismiss()
                            MaterialAlertDialogBuilder(this@AddBank).setTitle("Notification")
                                .setMessage(it.exception?.localizedMessage)
                                .setNegativeButton("Cancel") { dialog, which ->

                                }.setCancelable(false).show()
                        }
                    }
                }else{

                    loading.dismiss()
                    MaterialAlertDialogBuilder(this@AddBank).setTitle("Notification")
                        .setMessage("fields are empty or incorrect")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                }
            }
        }
    }
}
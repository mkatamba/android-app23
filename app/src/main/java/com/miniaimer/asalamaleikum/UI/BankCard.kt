package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.adapter.BankCardAdapter
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.app.App.Companion.db
import com.miniaimer.asalamaleikum.databinding.ActivityBankCardBinding
import com.miniaimer.data.Model.Bank
import com.miniaimer.domain.modal.BankWithID
import kotlinx.coroutines.launch

class BankCard : AppCompatActivity() {
    lateinit var binding: ActivityBankCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using the binding
        binding = ActivityBankCardBinding.inflate(layoutInflater) 
        setContentView(binding.root)
         // Create a loading dialog
        var loading = LoadingDialogFragment()
        binding.createBank.setOnClickListener {
            val modalBottomSheet = BankBottomSheet(null)
        // Show bottom sheet to create new bank card
            modalBottomSheet.show(supportFragmentManager, BankBottomSheet.TAG)


        }
        // Set navigation button on top app bar
        binding.run {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
             // Listen to changes in bank card data in Firestore
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "bank").addSnapshotListener { value, error ->
                // Show error message if there's an error
                if (error != null) {
                    MaterialAlertDialogBuilder(this@BankCard).setTitle("Notification")
                        .setMessage(error.localizedMessage)
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                }
                // Populate bank card list
                var list = arrayListOf<Bank>()
                value?.documents?.forEach {

                    var title = it.getString("title")
                    var content = it.getString("content")
                    var time = it.getLong("timed")
                    var typebank = it.getBoolean("banktype")
                    var obj = Bank(it.id, time!!, title!!, content!!, typebank!!)
                    list.add(obj)
                }
                // Set up RecyclerView and adapter
                val llm = LinearLayoutManager(this@BankCard)
                llm.setOrientation(LinearLayoutManager.VERTICAL)
                recyclerViewBank.setLayoutManager(llm)
                recyclerViewBank.adapter = BankCardAdapter(list, supportFragmentManager)

            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}
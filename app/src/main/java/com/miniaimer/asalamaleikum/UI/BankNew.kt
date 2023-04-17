package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.adapter.BankAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityBankNewBinding

class BankNew : AppCompatActivity() {
    lateinit var binding: ActivityBankNewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            createBanknew.setOnClickListener {
                startActivity(Intent(this@BankNew, AddBank::class.java))
            }
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "banknew").addSnapshotListener { value, error ->
                    if (error != null) {
                        MaterialAlertDialogBuilder(this@BankNew).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    }
                    var list = arrayListOf<com.miniaimer.domain.modal.BankNew>()
                    value?.documents?.forEach {

                        var number = it.getString("banknumber")
                        var bankname = it.getString("bankname")
                        var holdername = it.getString("holdername")
                        var cvv = it.getString("cvv")
                        var timed = it.getLong("timed")
                        var obj = com.miniaimer.domain.modal.BankNew(
                            it.id, number, bankname, holdername, timed, cvv
                        )
                        list.add(obj)
                    }
                    val llm = LinearLayoutManager(this@BankNew)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    recyclerViewBanknew.setLayoutManager(llm)
                    recyclerViewBanknew.adapter = BankAdapter(list)

                }
        }
    }
}
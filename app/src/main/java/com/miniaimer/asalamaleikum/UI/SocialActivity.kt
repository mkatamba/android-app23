package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.adapter.ReminderAdapter
import com.miniaimer.asalamaleikum.adapter.SocialAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivitySocialBinding
import com.miniaimer.domain.modal.Social

class SocialActivity : AppCompatActivity() {
    lateinit var binding: ActivitySocialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.run {
            floatingActionButton.setOnClickListener {
                val modalBottomSheet = SocialBottomSheet(null)
                modalBottomSheet.show(supportFragmentManager, SocialBottomSheet.TAG)
            }
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "social").addSnapshotListener { value, error ->
                    if (error != null) {
                        MaterialAlertDialogBuilder(this@SocialActivity).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()
                    }
                    var list = arrayListOf<Social>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var content = it.getString("content")
                        var pass = it.getString("pass")
                        var obj = Social(it.id, title!!, content!!, pass!!)
                        list.add(obj)
                    }
                    val llm = LinearLayoutManager(this@SocialActivity)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    listSocial.setLayoutManager(llm)
                    listSocial.adapter = SocialAdapter(list, supportFragmentManager)

                }
        }

    }
}
package com.miniaimer.asalamaleikum.UI

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.firestore.Query
import com.miniaimer.asalamaleikum.adapter.SearchAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityCalendarBinding
import com.miniaimer.domain.modal.Day
import java.text.SimpleDateFormat
import java.util.*

class Calendar : AppCompatActivity() {
    lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var list = mutableListOf<Day>()
        App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").orderBy(
            "time", Query.Direction.DESCENDING
        ).addSnapshotListener { value, error ->
            list.clear()
            value?.documents?.forEach {
                var title = it.getString("title")
                var content = it.getString("content")
                var type = it.getString("type")
                var time = it.getLong("time")
                var id = it.id
                var d = Day(id, type, title, content, time)
                list.add(d)
            }
            var newlist = mutableListOf<Day>()
            newlist.clear()
            list.forEach {
                if (it.time != null) {
                    if (it.time!!.toTimeDateString()
                            .equals(binding.calendar.date!!.toTimeDateString())
                    ) {
                        newlist.add(it)
                    }

                }

            }
            binding.topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            val llm = LinearLayoutManager(this@Calendar)
            llm.setOrientation(LinearLayoutManager.VERTICAL)
            binding.listzc.setLayoutManager(llm)
            binding.listzc.adapter = SearchAdapter(newlist)
        }


        binding.calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->
            var newlist = mutableListOf<Day>()
            list.forEach {
                if (it.time != null) {
                    var formatter = SimpleDateFormat("dd");
                    var dateString = formatter.format(Date(it.time!!))
                    var formatter2 = SimpleDateFormat("MM");
                    var dateString2 = formatter2.format(Date(it.time!!))
                    var formatter3 = SimpleDateFormat("yyyy");
                    var dateString3 = formatter3.format(Date(it.time!!))
                    if (dateString3.toInt() == i) {

                        Log.e("1", "1")
                        if (dateString2.toInt() == i2 + 1) {
                            Log.e("2", "2")
                            if (dateString.toInt() == i3) {
                                Log.e("3", "3")
                                newlist.add(it)
                            }
                        }
                    }
                }

            }
            val llm = LinearLayoutManager(this@Calendar)
            llm.setOrientation(LinearLayoutManager.VERTICAL)
            binding.listzc.setLayoutManager(llm)
            binding.listzc.adapter = SearchAdapter(newlist)
        }


    }

    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy")

        return format.format(dateTime)
    }
}
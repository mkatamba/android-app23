package com.miniaimer.asalamaleikum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.LoadingDialogFragment
import com.miniaimer.asalamaleikum.UI.ReminderBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Reminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReminderAdapter(private val dataSet: ArrayList<Reminder>,val sf : FragmentManager) :
    RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time : MaterialTextView
        var title : MaterialTextView
        var content : MaterialTextView
        var edit : AppCompatImageView
        var delete : AppCompatImageView
        init {
            time = view.findViewById(R.id.time)
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)
            edit = view.findViewById(R.id.edit)
            delete = view.findViewById(R.id.delete)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reminderitem, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = dataSet[position].title
        viewHolder.content.text = dataSet[position].content
        viewHolder.time.text = dataSet[position].time.toTimeDateString()
        viewHolder.edit.setOnClickListener {
            val modalBottomSheet = ReminderBottomSheet(dataSet[position].id)
            modalBottomSheet.show(sf, ReminderBottomSheet.TAG)
        }
        viewHolder.delete.setOnClickListener {
            var context = it.context
            MaterialAlertDialogBuilder(context).setTitle("Delete card")
                .setMessage("Do you want to remove this tag?")
                .setPositiveButton("Yes") { dialog, which ->
                    App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").document(dataSet[position].id).delete().addOnCompleteListener {
                        if (it.isSuccessful){
                            MaterialAlertDialogBuilder(context).setTitle("Notification")
                                .setMessage("Delete successfully")
                                .setNegativeButton("Close") { dialog, which ->

                                }.setCancelable(false).show()
                        }else{
                            MaterialAlertDialogBuilder(context).setTitle("Notification")
                                .setMessage(it.exception?.localizedMessage)
                                .setNegativeButton("Close") { dialog, which ->

                                }.setCancelable(false).show()
                        }

                    }
                }
                .setNegativeButton("No") { dialog, which ->

                }.setCancelable(false).show()

        }
    }
    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
        return format.format(dateTime)
    }
    override fun getItemCount() = dataSet.size

}

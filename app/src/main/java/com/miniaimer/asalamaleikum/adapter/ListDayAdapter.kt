package com.miniaimer.asalamaleikum.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.*
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Day
import com.miniaimer.domain.modal.Reminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ListDayAdapter(private val dataSet: ArrayList<Day>, val sf : FragmentManager) :
    RecyclerView.Adapter<ListDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time : MaterialTextView
        var title : MaterialTextView
        var type : AppCompatImageView
        var content : MaterialTextView

        var tzcz : ConstraintLayout
        init {
            time = view.findViewById(R.id.time)
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)
            type = view.findViewById(R.id.type)
            tzcz = view.findViewById(R.id.tzcz)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itemday, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
// Set the title, content, and time of the item in the ViewHolder
        viewHolder.title.text = dataSet[position].title
        viewHolder.content.text = dataSet[position].content
        viewHolder.time.text = dataSet[position].time?.toTimeDateString()
        // Set the appropriate icon based on the type of the item in the ViewHolder
        when(dataSet[position].type){
            "note" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_sticky_note_2_24))
            "appointment" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_calendar_month_24))
            "reminder" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_doorbell_24))
            "social" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_supervised_user_circle_24))
            "contact" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_contact_page_24))
            "image" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_image_24))
            "bank" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_credit_card_24))
            "banknew" -> viewHolder.type.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.baseline_credit_card_24))
        }
        // Get the context of the ViewHolder
        var context = viewHolder.itemView.context
        // Set an OnClickListener for the ViewHolder's tzcz button
        viewHolder.tzcz.setOnClickListener {    // Start the appropriate activity based on the type of the item
            when(dataSet[position].type){
                "note" -> it.context.startActivity(Intent(it.context, NoteActivity::class.java))
                "appointment" -> it.context.startActivity(Intent(it.context, AppointmentActivity::class.java))
                "reminder" -> it.context.startActivity(Intent(it.context, com.miniaimer.asalamaleikum.UI.Reminder::class.java))
                "social" -> {
                    MaterialAlertDialogBuilder(context).setTitle(
                        "Thông báo"
                    ).setMessage("This item requires a password, so you have to log in from the home page")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                }
                "contact" -> it.context.startActivity(Intent(it.context, ContactActivity::class.java))
                "image" -> it.context.startActivity(Intent(it.context, AlbumActivity::class.java))
                "bank" -> {
                    MaterialAlertDialogBuilder(context).setTitle(
                        "Thông báo"
                    ).setMessage("This item requires a password, so you have to log in from the home page")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                }

                "banknew" -> {
                    MaterialAlertDialogBuilder(context).setTitle(
                        "Thông báo"
                    ).setMessage("This item requires a password, so you have to log in from the home page")
                        .setNegativeButton("Cancel") { dialog, which ->
// The code seems to be missing the message for this case
                        }.setCancelable(false).show()
                }
            }
        }
    }
    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
        return format.format(dateTime)
    }
    override fun getItemCount() = dataSet.size

}

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
import com.miniaimer.asalamaleikum.UI.AppointmentBottomSheet
import com.miniaimer.asalamaleikum.UI.LoadingDialogFragment
import com.miniaimer.asalamaleikum.UI.ReminderBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Reminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentAdapter(private val dataSet: ArrayList<Reminder>, val sf : FragmentManager) :
    RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

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
// Create and return a new ViewHolder for the list item view
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reminderitem, viewGroup, false)

        return ViewHolder(view)
    }
// Bind the data at the specified position to the ViewHolder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Set the appointment title, content, and time text views to display the corresponding data
        viewHolder.title.text = "Appointment name: "+dataSet[position].title
        viewHolder.content.text ="Description: "+ dataSet[position].content
        viewHolder.time.text = dataSet[position].time.toTimeDateString()
        // Set a click listener for the edit button that shows an AppointmentBottomSheet dialog
        viewHolder.edit.setOnClickListener {
            val modalBottomSheet = AppointmentBottomSheet(dataSet[position].id)
            modalBottomSheet.show(sf, AppointmentBottomSheet.TAG)
        }
        // Set a click listener for the delete button that shows a confirmation dialog and deletes the corresponding appointment document
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
    // Extension function to convert a Long timestamp to a formatted date string
    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        // Format the date and time using the pattern "HH:mm dd/MM/yyyy"
        val format = SimpleDateFormat("HH:mm dd/MM/yyyy")
        return format.format(dateTime)
    }
    override fun getItemCount() = dataSet.size

}

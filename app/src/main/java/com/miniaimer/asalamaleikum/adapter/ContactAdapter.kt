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
import com.miniaimer.asalamaleikum.UI.ContactBottomSheet
import com.miniaimer.asalamaleikum.UI.LoadingDialogFragment
import com.miniaimer.asalamaleikum.UI.ReminderBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Reminder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ContactAdapter(private val dataSet: ArrayList<Reminder>, val sf : FragmentManager) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title : MaterialTextView
        var content : MaterialTextView
        var edit : AppCompatImageView
        var delete : AppCompatImageView
        var time : MaterialTextView
        // This section initializes the views used in each item of the RecyclerView
        init {
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)
            edit = view.findViewById(R.id.edit)
            delete = view.findViewById(R.id.delete)
            time = view.findViewById(R.id.time)
        }
    }
// This function is called when a new ViewHolder is created
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reminderitem, viewGroup, false)

        return ViewHolder(view)
    }
// This function is called when the RecyclerView is binding data to a ViewHolder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Hides the time TextView, as it is not used in this RecyclerView
        viewHolder.time.visibility = View.INVISIBLE
        viewHolder.title.text = "Contact name: "+dataSet[position].title
        viewHolder.content.text ="Phone Number: "+ dataSet[position].content
        // Sets up an OnClickListener for the edit button, which opens a bottom sheet when clicked
        viewHolder.edit.setOnClickListener {
            val modalBottomSheet = ContactBottomSheet(dataSet[position].id)
            modalBottomSheet.show(sf, ContactBottomSheet.TAG)
        }
        // Sets up an OnClickListener for the delete button, which deletes the item from the list when clicked
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

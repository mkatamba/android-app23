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
import com.miniaimer.asalamaleikum.UI.SocialBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Reminder
import com.miniaimer.domain.modal.Social
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SocialAdapter(private val dataSet: ArrayList<Social>, val sf : FragmentManager) :
    RecyclerView.Adapter<SocialAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var app : MaterialTextView
        var un : MaterialTextView
        var pass : MaterialTextView
        var edit : AppCompatImageView
        var delete : AppCompatImageView
        init {
            app = view.findViewById(R.id.app)
            un = view.findViewById(R.id.un)
            pass = view.findViewById(R.id.pass)
            edit = view.findViewById(R.id.edit)
            delete = view.findViewById(R.id.delete)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.socialitem, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.app.text ="App name: "+ dataSet[position].title
        viewHolder.un.text = "Username: "+ dataSet[position].content
        viewHolder.pass.text = "Password: "+dataSet[position].pass
        viewHolder.edit.setOnClickListener {
            val modalBottomSheet = SocialBottomSheet(dataSet[position].id)
            modalBottomSheet.show(sf, SocialBottomSheet.TAG)
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

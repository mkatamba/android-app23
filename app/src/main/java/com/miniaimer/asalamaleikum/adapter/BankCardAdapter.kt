package com.miniaimer.asalamaleikum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.BankBottomSheet
import com.miniaimer.asalamaleikum.UI.LoadingDialogFragment
import com.miniaimer.asalamaleikum.UI.ReminderBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.app.App.Companion.db
import com.miniaimer.asalamaleikum.databinding.BankSheetBinding
import com.miniaimer.data.Model.Bank
import com.miniaimer.domain.modal.BankWithID
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BankCardAdapter(private val dataSet: ArrayList<Bank> ,val sf : FragmentManager) :
    RecyclerView.Adapter<BankCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time : MaterialTextView
        var title : MaterialTextView
        var content : MaterialTextView
        var typebank : MaterialTextView
        var edit : AppCompatImageView
        var delete : AppCompatImageView
        init {
            time = view.findViewById(R.id.time)
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)
            edit = view.findViewById(R.id.edit)
            delete = view.findViewById(R.id.delete)
            typebank = view.findViewById(R.id.typebank)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_card_main, viewGroup, false)

        return ViewHolder(view)
    }
    // Sets the title, content, and time of the current ViewHolder to the corresponding data from the dataset
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = dataSet[position].title
        viewHolder.content.text = dataSet[position].content
        viewHolder.time.text = dataSet[position].time.toTimeDateString()
        if (dataSet[position].typebank){
             // If the type of the bank is cash, set the typebank TextView to "Cash"
            viewHolder.typebank.setText("Cash")
        }else{
 // If the type of the bank is card, set the typebank TextView to "Card"
            viewHolder.typebank.setText("Card")
        }
        viewHolder.edit.setOnClickListener {
            val modalBottomSheet = BankBottomSheet(dataSet[position].id)
            modalBottomSheet.show(sf, BankBottomSheet.TAG)
        }
        viewHolder.delete.setOnClickListener {
            var context = it.context
            MaterialAlertDialogBuilder(context).setTitle("Delete card")
                .setMessage("Do you want to remove this tag?")
                .setPositiveButton("Yes") { dialog, which -> // Delete the document from the "task" subcollection of the current user's document in the "users" collection
                    App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").document(dataSet[position].id).delete().addOnCompleteListener {
                         // If the deletion was successful, show a success notification; otherwise, show an error notification
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

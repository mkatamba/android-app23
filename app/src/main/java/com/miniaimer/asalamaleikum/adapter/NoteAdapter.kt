package com.miniaimer.asalamaleikum.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.editNote
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.Note
import java.util.*


class NoteAdapter(private val dataSet: ArrayList<Note>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content : MaterialTextView
        var title : MaterialTextView
        var delete : AppCompatImageView
        var noteupdate : MaterialCardView
        init {
            title = view.findViewById(R.id.title)
            content = view.findViewById(R.id.content)
            delete = view.findViewById(R.id.delete)
            noteupdate = view.findViewById(R.id.noteupdate)
        }
    }
// This function creates a new ViewHolder object by inflating the noteiten layout and returning it.
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.noteitem, viewGroup, false)

        return ViewHolder(view)
    }
    // This function binds the data to the ViewHolder object at the given position in the dataset.
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Set the title and content of the note at the given position in the dataset.
        viewHolder.title.text = dataSet[position].title
        viewHolder.content.text = dataSet[position].content
        // Set an onClickListener for the note update button to open the edit note activity with the note id as an extra.
        viewHolder.noteupdate.setOnClickListener {
            val i = Intent(it.context, editNote::class.java)
            i.putExtra("id", dataSet[position].id)
            it.context.startActivity(i)
        }
        // Set an onClickListener for the note delete button to display a confirmation dialog before deleting the note.
        viewHolder.delete.setOnClickListener {
            var context = it.context
            MaterialAlertDialogBuilder(context).setTitle("Delete card")
                .setMessage("Do you want to remove this note?")             // Delete the note from the Firebase Firestore database and show a success or error message.
                .setPositiveButton("Yes") { dialog, which ->
                    App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").document(dataSet[position].id).delete().addOnCompleteListener {
                        if (it.isSuccessful){
                            MaterialAlertDialogBuilder(context).setTitle("Notification")
                                .setMessage("Delete successfully")
                                .setNegativeButton("Close") { dialog, which ->

                                }.setCancelable(false).show()// If the delete operation is successful, a notification dialog is shown to the user to confirm the success, otherwise an error message is shown
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
    override fun getItemCount() = dataSet.size

}

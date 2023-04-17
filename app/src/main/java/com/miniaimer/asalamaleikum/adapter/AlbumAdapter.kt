package com.miniaimer.asalamaleikum.adapter
import android.content.Context
import com.bumptech.glide.request.target.Target
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.domain.modal.Album
import java.util.*

// Define a RecyclerView adapter for displaying a list of albums
class AlbumAdapter(private val dataSet: ArrayList<Album>,val context:Context) :
    RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {
// Define a ViewHolder for displaying individual album items
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image : AppCompatImageView  // ImageView for displaying album cover
        var delete : AppCompatImageView // ImageView for deleting the album
        var progres : CircularProgressIndicator // Progress indicator for loading album cover
        init {
            // Initialize the views for the ViewHolder
            delete = view.findViewById(R.id.delete)
            progres = view.findViewById(R.id.loading)
            image = view.findViewById(R.id.img)
        }
    }
// Create a ViewHolder object for each item in the dataset
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.imageitem, viewGroup, false)

        return ViewHolder(view)
    }
// Bind the data from the dataset to the views in the ViewHolder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            // Load the album cover image from Firebase Storage
            Firebase.storage.reference.child("images/"+App.auth.uid+"/"+dataSet[position].title).downloadUrl.addOnCompleteListener {
                Glide.with(context)
                    .load(it.result)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            Log.d("TAG", "OnResourceReady")
                            //do something when picture already loaded
                            viewHolder.image.setImageDrawable(resource)
                            viewHolder.progres.visibility = View.GONE

                            return true
                        }

                    })
                    .into(viewHolder.image)
            }

        }catch (e : Exception){

        }



        viewHolder.delete.setOnClickListener {
            var context = it.context
            MaterialAlertDialogBuilder(context).setTitle("Delete card")
                .setMessage("Do you want to remove this note?")
                .setPositiveButton("Yes") { dialog, which ->
                    App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task").document(dataSet[position].id).delete().addOnCompleteListener {
                        if (it.isSuccessful){
                            // If the image is successfully downloaded, load it into the ImageView using Glide
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
                // If the image fails to load, return false to allow Glide to display the error dialog
                }
                .setNegativeButton("No") { dialog, which ->

                }.setCancelable(false).show()
        }
    }
    // Return the number of items in the dataset
    override fun getItemCount() = dataSet.size

}

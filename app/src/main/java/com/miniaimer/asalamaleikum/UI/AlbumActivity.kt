package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.adapter.AlbumAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.databinding.ActivityAlbumBinding
import com.miniaimer.domain.modal.Album
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream


class AlbumActivity : AppCompatActivity() {
    // Declare a loading dialog fragment.
    var loading = LoadingDialogFragment()
    // Declare the binding to the activity layout file.
    lateinit var binding: ActivityAlbumBinding
    // Register an activity result contract for picking a visual media item.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        // Show a loading dialog and get a reference to the storage service.
        loading.show(supportFragmentManager, "loading")
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            var storageRef = App.storage.value.reference
            // Compress the image and convert it to a byte array.
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this@AlbumActivity.getContentResolver(), uri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val data = baos.toByteArray()
            // Upload the image to the storage service
            val riversRef = storageRef.child("images/${App.auth.uid}/${uri.lastPathSegment}.jpg")
            var uploadTask = riversRef.putBytes(data)

            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads

                loading.dismiss()
            }.addOnSuccessListener { taskSnapshot ->
             // If the upload is successful, create a reminder and add it to the database.
                val reminder = hashMapOf(
                    "title" to uri.lastPathSegment + ".jpg",
                    "time" to System.currentTimeMillis(),
                    "type" to "image"
                )
                App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                    .add(reminder).addOnCompleteListener {
                        if (it.isSuccessful) {
                            loading.dismiss()
                            MaterialAlertDialogBuilder(this@AlbumActivity).setTitle(
                                "Photo upload succeeded"
                            ).setMessage("Photo upload succeeded")
                                .setNegativeButton("Cancel") { dialog, which ->

                                }.setCancelable(false).show()
                        } else {

                            loading.dismiss() // Dismiss the loading dialog.

                        }
                    }

            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var loading = LoadingDialogFragment()
        loading.show(supportFragmentManager, "loading")
        binding.run {
            topAppBar.setNavigationOnClickListener {
                onBackPressed()
            }
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .whereEqualTo("type", "image").addSnapshotListener { value, error ->
                    if (error != null) {
                        MaterialAlertDialogBuilder(this@AlbumActivity).setTitle("Notification")
                            .setMessage(error.localizedMessage)
                            .setNegativeButton("Cancel") { dialog, which ->

                            }.setCancelable(false).show()  // Show an alert dialog to inform the user that the photo upload succeeded.
                    }
                    var list = arrayListOf<Album>()
                    value?.documents?.forEach {

                        var title = it.getString("title")
                        var obj = Album(it.id, title!!)
                        list.add(obj)
                    }
                    listAlbum.setLayoutManager(GridLayoutManager(this@AlbumActivity, 2))
                    listAlbum.adapter = AlbumAdapter(list, applicationContext)

                }
            lifecycleScope.launchWhenStarted {
                floatingActionButton.setOnClickListener {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
                delay(3000)
                loading.dismiss()
            }

        }
    }
}
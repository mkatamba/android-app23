package com.miniaimer.asalamaleikum.UI

import android.Manifest
import android.app.ActivityOptions
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.adapter.ListDayAdapter
import com.miniaimer.asalamaleikum.adapter.SearchAdapter
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.app.App.Companion.db
import com.miniaimer.asalamaleikum.databinding.ActivityMainBinding
import com.miniaimer.domain.modal.Day
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onPause() {
        super.onPause()


    }

    override fun onResume() {
        super.onResume()


    }

    val REQUEST_IMAGE_CAPTURE = 1553


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this


        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    startActivity(Intent(this@MainActivity, Weather::class.java))
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    startActivity(Intent(this@MainActivity, Weather::class.java))
                }
                else -> {
                    MaterialAlertDialogBuilder(this@MainActivity).setTitle("Notification")
                        .setMessage("You must grant location permission to see the weather")
                        .setNegativeButton("Cancel") { dialog, which ->

                        }.setCancelable(false).show()
                }
            }
        }

        lifecycleScope.launch {
            binding.weatherCard.setOnClickListener {

                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            var list = mutableListOf<Day>()
            App.db.collection("users").document(App.auth.currentUser!!.uid).collection("task")
                .addSnapshotListener { value, error ->
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
                }

            binding.searchvieww.getEditText().addTextChangedListener {

                    binding.searchBar.setText(binding.searchvieww.getText())
                    var newlist = mutableListOf<Day>()
                    list.forEach {
                        if (it.title != null && binding.searchvieww.getText() != null) {
                            if (it.title!!.contains(binding.searchvieww.getText()!!)) {
                                newlist.add(it)
                            }
                        }

                    }
                    val llm = LinearLayoutManager(this@MainActivity)
                    llm.setOrientation(LinearLayoutManager.VERTICAL)
                    binding.searchItem.setLayoutManager(llm)
                    binding.searchItem.adapter = SearchAdapter(newlist)
                }

            binding.searchvieww.getEditText().setOnEditorActionListener { v, actionId, event ->
                    binding.searchBar.setText(binding.searchvieww.getText())
                    binding.searchvieww.hide()
                    false
                }
        }

        lifecycleScope.launchWhenStarted {
            var loading = LoadingDialogFragment()
            with(binding) {
                bottomNavigation.setOnItemSelectedListener {
                    when (it.itemId) {
                        R.id.myday -> {
                            var i = Intent(this@MainActivity, ListDayActivity::class.java)
                            i.putExtra("day", "86400000")
                            startActivity(i)
                            true
                        }
                        R.id.servendays -> {
                            var i = Intent(this@MainActivity, ListDayActivity::class.java)
                            i.putExtra("day", "604800000")
                            startActivity(i)
                            true
                        }
                        R.id.allTasks -> {
                            var i = Intent(this@MainActivity, ListDayActivity::class.java)
                            i.putExtra("day", "0")
                            startActivity(i)
                            true
                        }
                        R.id.mycalendar -> {
                            val intent = Intent(this@MainActivity, Calendar::class.java)
                            startActivity(intent)
                            true
                        }
                        else -> false
                    }
                }

                banknew.setOnClickListener {
                    val view: View = layoutInflater.inflate(
                        com.miniaimer.asalamaleikum.R.layout.input_password, null
                    )
                    var k =
                        MaterialAlertDialogBuilder(this@MainActivity).setCancelable(false).create()
                    k.setView(view)
                    k.setOnShowListener(object : OnShowListener {
                        override fun onShow(p0: DialogInterface?) {
                            var textpas = view.findViewById<TextInputLayout>(R.id.inputpass)
                            var cancel = view.findViewById<MaterialButton>(R.id.cancelip)
                            var submit = view.findViewById<MaterialButton>(R.id.submitip)
                            cancel.setOnClickListener {
                                k.dismiss()
                            }
                            submit.setOnClickListener {
                                loading.show(supportFragmentManager, "loading")

                                db.collection("users").document(auth.currentUser!!.uid).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            var email = it.result.getString("email")
                                            auth.signInWithEmailAndPassword(
                                                email!!, textpas.editText?.text.toString().trim()
                                            ).addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    Log.d("TAGz", "signInWithEmail:success")
                                                    startActivity(
                                                        Intent(
                                                            this@MainActivity, BankNew::class.java
                                                        )
                                                    )
                                                    k.dismiss()
                                                } else {
                                                    Log.w(
                                                        "TAGz",
                                                        "signInWithEmail:failure",
                                                        it.exception
                                                    )
                                                    MaterialAlertDialogBuilder(this@MainActivity).setTitle(
                                                        "Sign Up Success"
                                                    ).setMessage("Incorrect password")
                                                        .setNegativeButton("Cancel") { dialog, which ->

                                                        }.setCancelable(false).show()

                                                }
                                                loading.dismiss()
                                            }


                                        } else {
                                            loading.dismiss()
                                            MaterialAlertDialogBuilder(this@MainActivity).setTitle("Sign Up Success")
                                                .setMessage(it.exception?.localizedMessage)
                                                .setNegativeButton("Cancel") { dialog, which ->
                                                }.setCancelable(false).show()
                                        }
                                    }
                            }
                        }

                    })
                    k.show()

                }
                noteCard.setOnClickListener {
                    startActivity(Intent(this@MainActivity, NoteActivity::class.java))
                }
                logout.setOnClickListener {
                    auth.signOut()
                    finishAffinity()
                    startActivity(Intent(this@MainActivity, SplashScreen::class.java))
                }
                camera.setOnClickListener {

                    startActivity(Intent(this@MainActivity, Shot::class.java))
                }
                albumCard.setOnClickListener {

                    startActivity(Intent(this@MainActivity, AlbumActivity::class.java))

                }
                socialCard.setOnClickListener {
                    val view: View = layoutInflater.inflate(
                        com.miniaimer.asalamaleikum.R.layout.input_password, null
                    )
                    var k =
                        MaterialAlertDialogBuilder(this@MainActivity).setCancelable(false).create()
                    k.setView(view)
                    k.setOnShowListener(object : OnShowListener {
                        override fun onShow(p0: DialogInterface?) {
                            var textpas = view.findViewById<TextInputLayout>(R.id.inputpass)
                            var cancel = view.findViewById<MaterialButton>(R.id.cancelip)
                            var submit = view.findViewById<MaterialButton>(R.id.submitip)
                            cancel.setOnClickListener {
                                k.dismiss()
                            }
                            submit.setOnClickListener {
                                loading.show(supportFragmentManager, "loading")

                                db.collection("users").document(auth.currentUser!!.uid).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            var email = it.result.getString("email")
                                            auth.signInWithEmailAndPassword(
                                                email!!, textpas.editText?.text.toString().trim()
                                            ).addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    Log.d("TAGz", "signInWithEmail:success")
                                                    startActivity(
                                                        Intent(
                                                            this@MainActivity,
                                                            SocialActivity::class.java
                                                        )
                                                    )
                                                    k.dismiss()
                                                } else {
                                                    Log.w(
                                                        "TAGz",
                                                        "signInWithEmail:failure",
                                                        it.exception
                                                    )
                                                    MaterialAlertDialogBuilder(this@MainActivity).setTitle(
                                                        "Sign Up Success"
                                                    ).setMessage("Incorrect password")
                                                        .setNegativeButton("Cancel") { dialog, which ->

                                                        }.setCancelable(false).show()

                                                }
                                                loading.dismiss()
                                            }


                                        } else {
                                            loading.dismiss()
                                            MaterialAlertDialogBuilder(this@MainActivity).setTitle("Sign Up Success")
                                                .setMessage(it.exception?.localizedMessage)
                                                .setNegativeButton("Cancel") { dialog, which ->
                                                }.setCancelable(false).show()
                                        }
                                    }
                            }
                        }

                    })
                    k.show()
                }
                reminderCard.setOnClickListener {

                    startActivity(Intent(this@MainActivity, Reminder::class.java))
                }
                appointmentCard.setOnClickListener {

                    startActivity(Intent(this@MainActivity, AppointmentActivity::class.java))
                }
                contactCard.setOnClickListener {

                    startActivity(Intent(this@MainActivity, ContactActivity::class.java))
                }
                bankCard.setOnClickListener {
                    val view: View = layoutInflater.inflate(
                        com.miniaimer.asalamaleikum.R.layout.input_password, null
                    )
                    var k =
                        MaterialAlertDialogBuilder(this@MainActivity).setCancelable(false).create()
                    k.setView(view)
                    k.setOnShowListener(object : OnShowListener {
                        override fun onShow(p0: DialogInterface?) {
                            var textpas = view.findViewById<TextInputLayout>(R.id.inputpass)
                            var cancel = view.findViewById<MaterialButton>(R.id.cancelip)
                            var submit = view.findViewById<MaterialButton>(R.id.submitip)
                            cancel.setOnClickListener {
                                k.dismiss()
                            }
                            submit.setOnClickListener {
                                loading.show(supportFragmentManager, "loading")
                                db.collection("users").document(auth.currentUser!!.uid).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            var email = it.result.getString("email")
                                            auth.signInWithEmailAndPassword(
                                                email!!, textpas.editText?.text.toString().trim()
                                            ).addOnCompleteListener {
                                                if (it.isSuccessful) {
                                                    Log.d("TAGz", "signInWithEmail:success")
                                                    startActivity(
                                                        Intent(
                                                            this@MainActivity, BankCard::class.java
                                                        )
                                                    )
                                                    k.dismiss()
                                                } else {
                                                    Log.w(
                                                        "TAGz",
                                                        "signInWithEmail:failure",
                                                        it.exception
                                                    )
                                                    MaterialAlertDialogBuilder(this@MainActivity).setTitle(
                                                        "Sign Up Success"
                                                    ).setMessage("Incorrect password")
                                                        .setNegativeButton("Cancel") { dialog, which ->

                                                        }.setCancelable(false).show()

                                                }
                                                loading.dismiss()
                                            }


                                        } else {
                                            loading.dismiss()
                                            MaterialAlertDialogBuilder(this@MainActivity).setTitle("Sign Up Success")
                                                .setMessage(it.exception?.localizedMessage)
                                                .setNegativeButton("Cancel") { dialog, which ->
                                                }.setCancelable(false).show()
                                        }
                                    }
                            }
                        }

                    })
                    k.show()
                }
                scrollview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    if (scrollY > 180) {

                    } else {
                    }
                }

            }
        }
    }

    fun showToastFab(view: FloatingActionButton, text: String) {
        view.setOnLongClickListener {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            true
        }

    }
}
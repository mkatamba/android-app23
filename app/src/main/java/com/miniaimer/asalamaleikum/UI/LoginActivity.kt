package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var loading = LoadingDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            gotoregister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            recoverypassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RecoveryPasswordActivity::class.java))
            }
            email.run {
                editText?.doOnTextChanged { inputText, _, _, _ ->
                    if (inputText?.length!! > 0) {
                        if (isValidEmail(inputText?.trim().toString())) {
                            error = null
                        } else {
                            error = "Invalid email"
                        }
                    } else {

                        error = null
                    }

                }
            }
            password.run {
                editText?.doOnTextChanged { inputText, _, _, _ ->
                    var repassword = editText?.text.toString().trim()
                    if (repassword.length > 5 || repassword.length == 0) {
                        error = null
                    } else {
                        error = "Must be 6 characters"
                    }

                }
            }
            btnlogin.setOnClickListener {
                if (password.error == null && email.error == null) {
                    var password = password.editText?.text.toString().trim()
                    var email = email.editText?.text.toString().trim()
                    if (password.length == 0 || email.length == 0) {
                        MaterialAlertDialogBuilder(this@LoginActivity).setTitle("Notification")
                            .setMessage("Fields cannot be left blank")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setCancelable(false).show()
                    } else {
                        Login(email, password)
                    }
                } else {
                    MaterialAlertDialogBuilder(this@LoginActivity).setTitle("Notification")
                        .setMessage("Please enter the correct request")
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setCancelable(false).show()
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun Login(email: String, password: String) {
        loading.show(supportFragmentManager, "loading")
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                loading.dismiss()
                Toast.makeText(
                    baseContext, "Logged in successfully", Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                loading.dismiss()
                MaterialAlertDialogBuilder(this@LoginActivity).setTitle("Notification")
                    .setMessage(task.exception?.localizedMessage)
                    .setNegativeButton("Cancel") { dialog, which ->
                    }.setCancelable(false).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}
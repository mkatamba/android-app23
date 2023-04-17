package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.databinding.ActivityRecoveryPasswordBinding
import java.util.regex.Pattern

class RecoveryPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecoveryPasswordBinding
    var loading = LoadingDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            backBtn.setOnClickListener {
                onBackPressed()
            }
            recoverymail.run {
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
            btnsubmit.setOnClickListener {
                if (recoverymail.error == null) {
                    var mail = recoverymail.editText?.text.toString().trim()
                    if (mail.length == 0) {
                        MaterialAlertDialogBuilder(this@RecoveryPasswordActivity).setTitle("Notification")
                            .setMessage("Fields cannot be left blank")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setCancelable(false).show()
                    } else {
                        SendNewPassword(mail)
                    }
                } else {
                    MaterialAlertDialogBuilder(this@RecoveryPasswordActivity).setTitle("Notification")
                        .setMessage("Please enter the correct request")
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setCancelable(false).show()
                }

            }
        }
    }

    fun SendNewPassword(emailAddress: String) {

        loading.show(supportFragmentManager, "loading")
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loading.dismiss()
                    MaterialAlertDialogBuilder(this@RecoveryPasswordActivity).setTitle("Notification")
                        .setMessage("A password reset request has been sent to your email. Please check your inbox and spam mail")
                        .setNegativeButton("Go to Login") { dialog, which ->
                            finish()
                        }.setCancelable(false).show()
                } else {
                    loading.dismiss()
                    MaterialAlertDialogBuilder(this@RecoveryPasswordActivity).setTitle("Notification")
                        .setMessage(task.exception?.localizedMessage)
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setCancelable(false).show()
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
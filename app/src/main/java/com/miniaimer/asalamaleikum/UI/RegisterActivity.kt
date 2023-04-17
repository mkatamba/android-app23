package com.miniaimer.asalamaleikum.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.app.App.Companion.db
import com.miniaimer.asalamaleikum.databinding.ActivityLoginBinding
import com.miniaimer.asalamaleikum.databinding.ActivityRegisterBinding
import com.miniaimer.asalamaleikum.helpers.TextHelper
import java.lang.reflect.Field
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    var checkBox = false
    var loading = LoadingDialogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            fullname.run {
                editText?.doOnTextChanged { inputText, _, _, _ ->
                    if (!inputText.isNullOrEmpty()) {
                        if (isFullname(inputText?.trim().toString())) {
                            error = null
                        } else {
                            error = "Invalid name"
                        }
                    } else {
                        error = null
                    }
                }
            }
            emailregister.run {
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
            repassword.run {
                editText?.doOnTextChanged { inputText, _, _, _ ->
                    var repassword = editText?.text.toString().trim()
                    if (repassword.length > 5 || repassword.length == 0) {
                        error = null
                        if (repassword.equals(passwordregister.editText?.text.toString().trim())) {
                            error = null
                        } else {
                            error = "Re-enter password does not match"
                        }
                    } else {
                        error = "Must be 6 characters"
                    }

                }
            }
            passwordregister.run {
                editText?.doOnTextChanged { inputText, _, _, _ ->
                    var password = editText?.text.toString().trim()
                    if (password.length > 5 || password.length == 0) {
                        error = null
                    } else {
                        error = "Must be 6 characters"
                    }

                }
            }

            TextHelper().customTextPrivacy(textprivacyregister, this@RegisterActivity)
            gotologin.setOnClickListener {
                finish()
            }
            checkboxdk.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    btnregister.run {
                        alpha = 1f
                    }
                } else {
                    btnregister.run {
                        alpha = 0.5f
                    }
                }
                checkBox = b
            }
            btnregister.setOnClickListener {
                if (checkBox) {
                    if (repassword.error == null && passwordregister.error == null && fullname.error == null && emailregister.error == null) {
                        var password = passwordregister.editText?.text.toString().trim()
                        var rePassword = repassword.editText?.text.toString().trim()
                        var fullName = fullname.editText?.text.toString().trim()
                        var emailRegister = emailregister.editText?.text.toString().trim()
                        if (password.length == 0 || rePassword.length == 0 || fullName.length == 0 || emailRegister.length == 0) {
                            MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                                .setMessage("Fields cannot be left blank")
                                .setNegativeButton("Cancel") { dialog, which ->
                                }.setCancelable(false).show()
                        } else {
                            if (password.equals(rePassword)) {
                                CreateUser(emailRegister, password)
                            } else {
                                MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                                    .setMessage("Re-enter password does not match")
                                    .setNegativeButton("Cancel") { dialog, which ->
                                    }.setCancelable(false).show()

                            }

                        }
                    } else {
                        MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                            .setMessage("Please enter the correct request")
                            .setNegativeButton("Cancel") { dialog, which ->
                            }.setCancelable(false).show()
                    }
                } else {
                    MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                        .setMessage("You must accept the terms")
                        .setNegativeButton("Cancel") { dialog, which ->
                        }.setCancelable(false).show()
                }
            }
        }
    }

    fun CreateUser(email: String, password: String) {
        loading.show(supportFragmentManager, "loading")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = binding.fullname.editText?.text.toString().trim()
                }
                user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userData = hashMapOf(
                                "name" to user.displayName,
                                "email" to user.email,
                                "password" to password,
                                "createTime" to FieldValue.serverTimestamp()
                            )
                            db.collection("users").document(auth.currentUser!!.uid).set(userData)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        loading.dismiss()
                                        MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Sign Up Success")
                                            .setMessage(task.exception?.localizedMessage)
                                            .setNegativeButton("Go to dashboard") { dialog, which ->
                                                finish()
                                            }.setCancelable(false).show()
                                    } else {
                                        MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                                            .setMessage(task.exception?.localizedMessage)
                                            .setNegativeButton("Cancel") { dialog, which ->
                                            }.setCancelable(false).show()
                                    }
                                }
                        } else {
                            MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
                                .setMessage(task.exception?.localizedMessage)
                                .setNegativeButton("Cancel") { dialog, which ->
                                }.setCancelable(false).show()
                        }
                    }
            } else {
                loading.dismiss()
                MaterialAlertDialogBuilder(this@RegisterActivity).setTitle("Notification")
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

    fun isFullname(str: String): Boolean {
        if (str.trim().length > 2) {
            return true
        } else {
            return false
        }
    }
}
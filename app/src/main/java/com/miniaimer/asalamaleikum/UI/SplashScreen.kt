package com.miniaimer.asalamaleikum.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.databinding.ActivitySplashScreenBinding
import com.miniaimer.asalamaleikum.helpers.CheckOnline
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.run {
            txtLoading.text = "Check network connection..."
            checkConnect()
            lifecycleScope.launch {
                coroutineScope {
                    delay(1000)
                    txtLoading.text = "Identity check..."
                    delay(1000)
                    if (auth.currentUser != null) {
                        startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    fun checkConnect() {
        if (CheckOnline().isOnline(this@SplashScreen)) {
            return
        }
        MaterialAlertDialogBuilder(this@SplashScreen).setTitle("Error")
            .setMessage("No internet connection").setPositiveButton("Again") { dialog, which ->
                dialog.dismiss()
                checkConnect()
            }.setNegativeButton("Exit") { dialog, which ->
                finishAffinity()
            }.setCancelable(false).show()

    }
}
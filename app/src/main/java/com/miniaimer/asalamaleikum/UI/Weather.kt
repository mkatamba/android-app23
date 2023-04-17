package com.miniaimer.asalamaleikum.UI

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.adapter.BankCardAdapter
import com.miniaimer.asalamaleikum.adapter.WeatherCardAdapter
import com.miniaimer.asalamaleikum.databinding.ActivityWeatherBinding
import com.miniaimer.domain.modal.CurrentWeather
import com.miniaimer.domain.modal.ListHourly
import com.miniaimer.domain.modal.WeatherData
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Weather : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    private lateinit var locationCallback: LocationCallback
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private fun startLocationUpdates() {

    }

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    lateinit var binding: ActivityWeatherBinding
    lateinit var mLocationRequest: LocationRequest
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }

    lateinit var loading: LoadingDialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingDialogFragment()
        loading.show(supportFragmentManager, "l")
        lifecycleScope.launch {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@Weather);
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                }
            }
            createLocationRequest();
            getLastLocation();
            binding.run {
                topAppBar.setNavigationOnClickListener {
                    onBackPressed()
                }
            }


        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        var mLocation = task.result
                        val geocoder: Geocoder
                        val addresses: List<Address>

                        geocoder = Geocoder(this, Locale.getDefault())

                        addresses = geocoder.getFromLocation(
                            mLocation.latitude, mLocation.longitude, 1
                        )!!
                        try {
                            val address: String = addresses.get(0).getAddressLine(0)
                            address?.let {
                                binding.textlocation.text = address
                            }
                        } catch (e: Exception) {
                        }

                        var URL =
                            "https://api.open-meteo.com/v1/forecast?latitude=" + mLocation.latitude + "&longitude=" + mLocation.longitude + "&hourly=temperature_2m,relativehumidity_2m,rain,weathercode&daily=weathercode,temperature_2m_max&current_weather=true&timeformat=unixtime&past_days=1&forecast_days=3&timezone=auto"
                        val queue = Volley.newRequestQueue(this)
                        val stringRequest = StringRequest(Request.Method.GET, URL, { response ->
                            val gson = Gson()
                            val weatherData: WeatherData =
                                gson.fromJson(response.toString(), WeatherData::class.java)
                            val currentWeather = weatherData.current_weather
                            val daily = weatherData.daily
                            val hourly = weatherData.hourly
                            binding.imgsun.run {
                                when (currentWeather.weathercode) {
                                    0f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.sun
                                            )
                                        )
                                    }
                                    1f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.sunmay
                                            )
                                        )
                                    }
                                    2f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.sunmay
                                            )
                                        )
                                    }
                                    3f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.sunmay
                                            )
                                        )
                                    }
                                    45f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.suongmu
                                            )
                                        )
                                    }
                                    48f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.suongmu
                                            )
                                        )
                                    }
                                    51f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    53f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    55f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    56f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    57f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    61f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    63f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    65f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    66f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    67f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    80f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    81f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                    82f -> {
                                        setImageDrawable(
                                            resources.getDrawable(
                                                R.drawable.mua
                                            )
                                        )
                                    }
                                }
                            }
                            hourly?.let {

                                var listHourlyArray = arrayListOf<ListHourly>()
                                it.time.forEach {
                                    var indexItem = hourly.time.indexOf(it)
                                    var ListHourly = ListHourly(
                                        hourly.time[indexItem],
                                        hourly.temperature_2m[indexItem],
                                        hourly.weathercode[indexItem],
                                        hourly.relativehumidity_2m[indexItem],
                                        hourly.rain[indexItem]
                                    )
                                    listHourlyArray.add(ListHourly)
                                    if (it.toTimeDateString() == currentWeather.time.toTimeDateString()) {
                                        var index = hourly.time.indexOf(it)
                                        binding.run {
                                            raincurrent.text = hourly.rain[index].toString() + " mm"
                                            Humidity.text =
                                                hourly.relativehumidity_2m[index].toString() + " %"
                                            windspeed.text =
                                                currentWeather.windspeed.toString() + " Km/h"
                                        }
                                    }
                                }
                                val llm = LinearLayoutManager(this@Weather)
                                llm.setOrientation(LinearLayoutManager.HORIZONTAL)
                                binding.listHourly.setLayoutManager(llm)
                                binding.listHourly.adapter = WeatherCardAdapter(listHourlyArray)
                                binding.listHourly.getLayoutManager()?.scrollToPosition(32);


                            }
                            currentWeather?.let {
                                binding.nhietdo.text = it.temperature.toInt().toString() + "°"
                                loading.dismiss()
                            }
                        }, { })
                        queue.add(stringRequest)
                    } else {

                    }
                }
        } catch (unlikely: SecurityException) {

        }
    }

    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat("HH dd/MM/yyyy")
        return format.format(dateTime)
    }

    fun findweather() {

    }
}
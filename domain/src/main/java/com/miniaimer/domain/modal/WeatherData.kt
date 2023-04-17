package com.miniaimer.domain.modal

import org.json.JSONObject

data class WeatherData(val current_weather : CurrentWeather,val hourly : Hourly,val daily : Daily)
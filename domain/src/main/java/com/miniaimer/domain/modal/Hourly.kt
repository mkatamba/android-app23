package com.miniaimer.domain.modal

data  class Hourly(val time : List<Long>,val temperature_2m : List<Float>,val weathercode : List<Float>,val relativehumidity_2m : List<Float>,val rain : List<Float>)
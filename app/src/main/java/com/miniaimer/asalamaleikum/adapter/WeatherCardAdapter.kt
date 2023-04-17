package com.miniaimer.asalamaleikum.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.LoadingDialogFragment
import com.miniaimer.asalamaleikum.app.App.Companion.auth
import com.miniaimer.asalamaleikum.app.App.Companion.db
import com.miniaimer.data.Model.Bank
import com.miniaimer.domain.modal.BankWithID
import com.miniaimer.domain.modal.Hourly
import com.miniaimer.domain.modal.ListHourly
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherCardAdapter(private val dataSet: ArrayList<ListHourly>) :
    RecyclerView.Adapter<WeatherCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var hourlytext: MaterialTextView
        var nhietdohourly: MaterialTextView
        var imghourly: AppCompatImageView

        init {
            hourlytext = view.findViewById(R.id.hourlytext)
            nhietdohourly = view.findViewById(R.id.nhietdohourly)
            imghourly = view.findViewById(R.id.imghourly)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.itemhourly, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.hourlytext.text = convertLongToTime(dataSet[position].time)
        viewHolder.nhietdohourly.text = dataSet[position].temperature_2m.toString() + "°"
        viewHolder.imghourly.run {
            when (dataSet[position].weathercode) {
                0f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.sun))
                }
                1f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.sunmay))
                }
                2f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.sunmay))
                }
                3f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.sunmay))
                }
                45f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.suongmu))
                }
                48f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.suongmu))
                }
                51f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                53f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                55f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                56f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                57f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                61f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                63f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                65f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                66f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                67f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                80f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                81f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
                82f -> {
                    setImageDrawable(viewHolder.itemView.resources.getDrawable(R.drawable.mua))
                }
            }
        }

    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time * 1000)
        val format = SimpleDateFormat("HH:mm dd.MM")
        return format.format(date)
    }

    override fun getItemCount() = dataSet.size

}

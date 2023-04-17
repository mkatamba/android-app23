package com.miniaimer.asalamaleikum.app

import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class AdapterBindingApp {
    companion object{
        @JvmStatic
        @BindingAdapter("app:setOnLongClickListenerz")
        fun setOnLongClickListenerz(view: ExtendedFloatingActionButton,boolean: Boolean) {
            view.setOnLongClickListener {
                if (view.isExtended) {
                    view.shrink()
                } else {
                    view.extend()
                }
                true
            }
        }


    }
}
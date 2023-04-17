package com.miniaimer.asalamaleikum.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.miniaimer.asalamaleikum.R


class LoadingDialogFragment: DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
         container: ViewGroup?,
         savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.loading, container, false)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.window!!.setDimAmount(0.8f)
        this.isCancelable = false
        return view
    }



}
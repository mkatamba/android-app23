package com.miniaimer.asalamaleikum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.BankBottomSheet
import com.miniaimer.asalamaleikum.app.App
import com.miniaimer.data.Model.Bank
import com.miniaimer.domain.modal.BankNew
import java.text.SimpleDateFormat

class BankAdapter (private val dataSet: ArrayList<BankNew>) :
    RecyclerView.Adapter<BankAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define variables for the views in the item layout
        var expires : MaterialTextView
        var bankName : MaterialTextView
        var cardnumber : MaterialTextView
        var nameholder : MaterialTextView
        var banklogo : AppCompatImageView
        var cvv : MaterialTextView
        // Initialize the variables with the corresponding views from the item layout
        init {
            expires = view.findViewById(R.id.expiress)
            bankName = view.findViewById(R.id.bankName)
            cardnumber = view.findViewById(R.id.cardnumber)
            banklogo = view.findViewById(R.id.banklogo)
            nameholder = view.findViewById(R.id.nameholder)
            cvv = view.findViewById(R.id.cvv)
        }
    }
    // Create the view holder for the item layout
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and return the view holder
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itembank, viewGroup, false)

        return ViewHolder(view)
    }
    // Bind data to the views in the item layout for the given position
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Set the name holder text to the corresponding data
        viewHolder.nameholder.text = dataSet[position].name
        viewHolder.bankName.text = dataSet[position].bankName // Set the bank name text to the corresponding data
        var list = dataSet[position].number?.chunked(4) // Format the card number and set it to the corresponding view
        viewHolder.cardnumber.text = list!![0] +" "+ list!![1] +" " + list!![2] +" " +list!![3]
        viewHolder.cvv.text ="Cvv: "+ dataSet[position].cvv  // Set the CVV text to the corresponding data
// Set the bank logo image according to the first digits of the card number
        var first = list[0].chunked(2)
        var num = first!![0].toInt()
        if (num >= 50 && num<=55){
            viewHolder.banklogo.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.mclogo))
        }else
        if (num >= 41 && num <=49)
        {
            viewHolder.banklogo.setImageDrawable(viewHolder.itemView.context.resources.getDrawable(R.drawable.visalogo))
        }else{

            viewHolder.banklogo.visibility  = View.INVISIBLE
        }
        // Format the expiration date and set it to the corresponding view
        viewHolder.expires.text = dataSet[position].expires?.toTimeDateString()
    }
    fun Long.toTimeDateString(): String {
        val dateTime = java.util.Date(this)
        val format = SimpleDateFormat("MM/yyyy")
        return format.format(dateTime)
    }
    override fun getItemCount() = dataSet.size

}
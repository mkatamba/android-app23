package com.miniaimer.asalamaleikum.helpers

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.miniaimer.asalamaleikum.R
import com.miniaimer.asalamaleikum.UI.LoginActivity

class TextHelper {
    var agree = "I agree to the "
    var community = "Community Guidelines"
    var privacy = "Privacy Policy"
    var term = "Terms & Conditions"
    var and = "and"
    fun customTextPrivacy(view: TextView, activity:Activity) {
        val spanTxt = SpannableStringBuilder(
            agree
        )
        spanTxt.append(" "+term)
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

        },agree.length, spanTxt.length, 0)
        spanTxt.append(",")

        spanTxt.append(" "+community)
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

            }
        },
            agree.length + term.length +2, spanTxt.length, 0)
        spanTxt.append(" "+and)
        spanTxt.setSpan(ForegroundColorSpan(Color.GRAY), agree.length + term.length + community.length +3, spanTxt.length, 0)
        spanTxt.append(" "+privacy)
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {

            }
        }, spanTxt.length - privacy.length , spanTxt.length, 0)
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }
}
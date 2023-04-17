package com.miniaimer.domain.modal

data class BankWithID (val id : String, val number : String? = null, val bankName:String?=null, val name : String? = null, val expires : Long ? = null, val cvv : String? = null)
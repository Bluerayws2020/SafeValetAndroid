package com.example.safevalet.adapters


interface OnClickListener {


    fun selectCar(pid: String, cid: String, lang: String)

    fun updateCar(pid: String, lang: String?, nickname:String?
                  , carMake:String?, carModel:String?,
                  year:String?, plateNo:String?)

    fun removeCar(pid: String)


    }

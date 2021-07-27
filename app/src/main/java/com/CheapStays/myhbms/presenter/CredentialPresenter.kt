package com.CheapStays.myhbms.presenter

import android.view.View
import com.CheapStays.myhbms.RegisterFragment
import com.CheapStays.myhbms.model.User

import com.CheapStays.myhbms.view.ILogInView

class CredentialPresenter : IPresenter {

    val adminEmail : String = ""
    val password : String = ""
    val uid : String = ""

    // getting reference of Fragment Class
    var fragView : ILogInView


    constructor(fragView: ILogInView){
        this.fragView = fragView
    }



    override fun onLogin(email: String, pass: String): Int{
        // having object of model class
        val user = User(email,pass)

        val isLogInCode = user.isvalidData()
        return isLogInCode
    }


}
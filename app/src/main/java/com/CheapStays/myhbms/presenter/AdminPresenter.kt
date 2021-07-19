package com.CheapStays.myhbms.presenter

import com.CheapStays.myhbms.LogInFragment
import com.CheapStays.myhbms.model.IUser
import com.CheapStays.myhbms.model.User
import com.CheapStays.myhbms.view.ILogInView

class AdminPresenter: LogInpresenter{

    var logInView : LogInFragment


    constructor(logInView: LogInFragment){
        this.logInView = logInView
    }
    override fun checkAdmin(email: String, pass: String): Boolean {
        val user = User(email,pass)  // creating object of Model Class
        val admin = user.isAdmin(email,pass)  // accessing method of User model class
        return  admin
    }


}
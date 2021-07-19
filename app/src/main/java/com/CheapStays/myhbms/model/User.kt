package com.CheapStays.myhbms.model

import android.util.Patterns
import java.util.regex.Pattern

class User() : IUser{
    private var isAdmin = false
    private var adminmail : String ="admin@gmail.com"
    private var adminpass : String = "123456789"
    private var email : String = ""
    private var pass : String = ""
   constructor(email : String, pass : String) : this(){
       this.email = email
       this.pass = pass

   }

    override fun getEmail(): String {
        return email
    }

    override fun getPassword(): String {
        return pass
    }

    override fun isvalidData(): Int {
        if(getEmail().isEmpty())
        {return 0}
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        { return  1}
        else if(pass.length <=8)
        {return 2}

        else
        {return -1}

    }

    override fun isAdmin(email : String ,pass : String): Boolean {
        if(email== adminmail && pass == adminpass){
            isAdmin = true
        }
        return isAdmin
    }
}
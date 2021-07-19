package com.CheapStays.myhbms.model

interface IUser {

    fun getEmail() : String
    fun getPassword() : String
    fun isvalidData() : Int
    fun isAdmin(email : String, pass : String) : Boolean
}
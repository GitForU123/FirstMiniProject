package com.CheapStays.myhbms.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.CheapStays.myhbms.R
import com.CheapStays.myhbms.RegisterFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        val Introfrag = IntroFragment()
        transaction.add(R.id.mapL,Introfrag)
        transaction.commit()
    }
}
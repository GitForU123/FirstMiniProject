package com.CheapStays.myhbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        val registerfrag = RegisterFragment()
        transaction.add(R.id.parentL,registerfrag)
        transaction.commit()
    }
}
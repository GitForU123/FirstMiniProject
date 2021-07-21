package com.CheapStays.myhbms.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.CheapStays.myhbms.MapsActivity
import com.CheapStays.myhbms.R

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
    }

    fun mapClicked(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
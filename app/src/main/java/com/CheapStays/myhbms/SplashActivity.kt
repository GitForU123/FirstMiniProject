package com.CheapStays.myhbms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.CheapStays.myhbms.view.AdminActivity
import com.CheapStays.myhbms.view.MainActivity
import com.CheapStays.myhbms.view.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        Timer().schedule(3000){
//            val intent = Intent(this@SplashActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()

        Handler().postDelayed({
            // Get the current user id from firestore
            var currentUserID = FirebaseAuth.getInstance().currentUser?.uid
//            if(currentUserID == "owUHYkK4CmQqxc7zJEJO3bYRc2X2"){
//                startActivity(Intent(this,AdminActivity::class.java))
            if(currentUserID != null){
                if(currentUserID == "FQq0vKB4MMNxlncS62Y6GQWf1iD3"){
                    startActivity(Intent(this,AdminActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this,UserHomeActivity::class.java))
                    finish()
                }

            }else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        },2000)


    }
}
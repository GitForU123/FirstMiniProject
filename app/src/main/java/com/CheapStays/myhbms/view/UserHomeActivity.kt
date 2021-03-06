package com.CheapStays.myhbms.view

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.CheapStays.myhbms.R
import com.google.android.material.navigation.NavigationView


class UserHomeActivity : AppCompatActivity() {

   lateinit var navView : BottomNavigationView
   lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val navController = Navigation.findNavController(this,R.id.navigation_home)
        setContentView(R.layout.activity_user_home)
        navView = findViewById(R.id.nav_view)



        val navController =
            findNavController(R.id.nav_host_fragment_activity_user_home) // setting navigation
        // to host fragment which is associated with user home activity

        NavigationUI.setupActionBarWithNavController(this,navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupWithNavController(navView,navController)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_order, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // hides the bottom navigation when on different fragment other than toplevel destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.navigation_home ||
                destination.id == R.id.navigation_account ||
                destination.id == R.id.navigation_cart ||
                destination.id == R.id.navigation_order) {

                navView.visibility = View.VISIBLE
            } else {

                navView.visibility = View.GONE
            }
        }
    }
    // associates the arrow with launching fragment
    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_home)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


}
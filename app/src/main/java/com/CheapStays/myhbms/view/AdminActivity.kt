package com.CheapStays.myhbms.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class AdminActivity : AppCompatActivity() {
    lateinit var db: FirebaseDatabase
    lateinit var rView : RecyclerView
    lateinit var floatButton : FloatingActionButton
    lateinit  var hotelList : ArrayList<Hotel>
    lateinit var mProgressDialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        showProgressDialog()
        hotelList = arrayListOf()

        rView = findViewById(R.id.rView)

        floatButton = findViewById(R.id.fabB)

        rView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database

        getHotelDetails()




        registerForContextMenu(rView)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("SignOut")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "SignOut" ->{
                // sign out here
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showProgressDialog() {
        mProgressDialog =  Dialog(this)


        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)

        mProgressDialog.show()
    }

    fun fabClicked(view: View) {
        val intent = Intent(this,HotelDetailsActivity::class.java)
        startActivity(intent)
    }

    private fun getHotelDetails() {
        val ref = db.getReference("HotelDB").child("Hotel")   // to go to Hotel node in db

        ref.addValueEventListener(object : ValueEventListener{  // adding a listener to this node for any data change
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AdminActivity", "${snapshot.value}")
                hideProgressDialog()
                hotelList.clear()
                for (hotelSnapshot in snapshot.children) {
                    Log.d("AdminActivity", "$hotelSnapshot")
                    val hotel =
                        hotelSnapshot.getValue(Hotel::class.java) // value in it will be details of hotel

                    if (hotel != null) {
                        Log.d(
                            "AdminActivity", "hotel id : ${hotel.id} &" +
                                    "hotel name : ${hotel.name}  & ${hotel.city} " +
                                    "& url ${hotel.url} "
                        )
                    }

                    hotelList.add(hotel!!)

                }
                rView.adapter = HotelListAdapter(hotelList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }


}




 class Hotel(){
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0
    var cuisine : List<String>? = null
    var url : String = ""
    constructor( id : Int , name : String, city : String, lat : Double , long : Double,
                 cuisine : List<String>, url : String
                ) : this(){
                    this.id = id
                    this.name =name
                    this.city = city
                    this.lat = lat
                    this.long = long
                    this.cuisine = cuisine
                    this.url = url
                }
      }





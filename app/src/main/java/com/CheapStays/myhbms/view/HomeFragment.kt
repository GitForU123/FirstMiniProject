package com.CheapStays.myhbms.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.MapsActivity
import com.CheapStays.myhbms.R
import android.location.LocationListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment() : Fragment(){



    lateinit var db : FirebaseDatabase
    lateinit var rView : RecyclerView
    lateinit  var hotelList : ArrayList<HotelForUser>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Firebase.database
        hotelList = arrayListOf()



    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val imageButton = view.findViewById<ImageButton>(R.id.imageButton)
        rView = view.findViewById(R.id.userRV)

        rView.layoutManager = LinearLayoutManager(context)
        getHotelList()

        imageButton.setOnClickListener{
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)
        }







        super.onViewCreated(view, savedInstanceState)
    }

    private fun getHotelList(){
        val ref = db.getReference("HotelDB").child("Hotel")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hotelList.clear()
                for (hotelSnapshot in snapshot.children) {

                    val hotel = hotelSnapshot.getValue(HotelForUser::class.java)

                    if (hotel != null) {
                        hotelList.add(hotel)
                    }

                }

                rView.adapter = HotelAdapter(hotelList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



}

class HotelForUser(){
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
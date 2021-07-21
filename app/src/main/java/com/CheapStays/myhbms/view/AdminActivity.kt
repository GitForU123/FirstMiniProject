package com.CheapStays.myhbms.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    var counter : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        hotelList = arrayListOf()

        rView = findViewById(R.id.rView)

        floatButton = findViewById(R.id.fabB)

        rView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database

        getHotelDetails()

        getCuisineList(1)

//        addHotel()
        registerForContextMenu(rView)

    }

//    override fun onResume() {
//        super.onResume()
//        getHotelDetails()
//    }

    fun fabClicked(view: View) {
        val intent = Intent(this,HotelDetailsActivity::class.java)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 1){
            when(resultCode){
                RESULT_OK ->{
                    data?.let {
                        val hotelName = it.getStringExtra("name")?: ""
                        val id = it.getIntExtra("id",11)
                        val city =it.getStringExtra("city")  ?: ""
                        val lat = it.getDoubleExtra("lat",0.0)
                        val long = it.getDoubleExtra("lon",0.0)

                        val cuisinetype = it.getStringArrayListExtra("cuisinetype") as List<String>
//                        val itemno = it.getIntExtra("itemid",0)
//                        val itemdescription = it.getStringExtra("itemdescription") ?: ""
//                        val itemprice = it.getIntExtra("itemprice",500)

                        addHotel(hotelName,id,city,lat,long,cuisinetype)

                        Toast.makeText(this,"Received $hotelName & $city & $lat & $long" +
                                "& $cuisinetype",
                            Toast.LENGTH_SHORT).show()
                    }



                }
                RESULT_CANCELED ->{
                    Toast.makeText(this,"Nothing Received",Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    private fun getHotelDetails() {
        val ref = db.getReference("HotelDB").child("Hotel")   // to go to Hotel node in db

        ref.addValueEventListener(object : ValueEventListener{  // adding a listener to this node for any data change
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AdminActivity", "${snapshot.value}")
                hotelList.clear()
                for (hotelSnapshot in snapshot.children) {
                    Log.d("AdminActivity", "$hotelSnapshot")
                    val hotel =
                        hotelSnapshot.getValue(Hotel::class.java)   // value in it will be details of hotel

                    if (hotel != null) {
                        val lat = hotel.lat
                        val long = hotel.long



                        Log.d(
                            "AdminActivity", "hotel id : ${hotel.id} &" +
                                    "hotel name : ${hotel.name}  & ${hotel.city}  "
                        )
                    }

                    hotelList.add(hotel!!)

                }
                rView.adapter = HotelListAdapter(hotelList)
            }




//                val hotelobj = snapshot.child("hotel")  //going to child node
//
//                // doing iteration over the hotel list
//                hotelobj.children.forEach {
//                    val hotelList = it.getValue(Hotel::class.java)  // collecting value in Hotel Class
//
//
//                    Log.d("AdminActivity","hotel id : ${hotelList?.id} &" +
//                            "hotel name : ${hotelList?.name}  & ${hotelList?.city}")
//
//                    val cuisineList = hotelList?.cuisine
//                    cuisineList?.forEach {
//
//                        Log.d("AdminActivity","type : ${it.type}")
//
//                        val itemList = it.items
//                        itemList?.forEach {
//                            Log.d("AdminActivity","price : ${it.price} & " +
//                                    "description : ${it.description}")
//                        }


//                val cuisineobj = hotelobj.child("cuisine")
//                cuisineobj.children.forEach {
//                    Log.d("AdminActivity","${cuisineobj}")
//                }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    private fun getCuisineList(hotelid : Int){

        val ref = db.getReference("HotelDB").child("Hotel").child("$hotelid").child("cuisinetype")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Cuisine", "${snapshot.value}")
                for (list in snapshot.children){
                    Log.d("Cuisine", "$list")   // list of cuisinetype
                    for(cuisineList in list.children)
                    {
                        Log.d("Cuisine", "$cuisineList")   // list of items
                        val cuisineValue = cuisineList.getValue(CuisineList::class.java)
                        Log.d("Cuisine", "${cuisineValue?.cuisinetype}  & ${cuisineValue?.itemNo} &" +
                                "${cuisineValue?.description} & ${cuisineValue?.price}")

                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

    private fun addHotel(hotelName : String ,id : Int, city : String, lat : Double,
                         long : Double, cuisinetype : List<String>)
      {
        val cuisineList = mutableListOf<String>()
          for( type in cuisinetype){
              cuisineList.add(type)
          }

       val hotel = Hotel(id,hotelName,city,lat,long,cuisineList)

          val ref = db.getReference("HotelDB")
        ref.child("Hotel").child(id.toString()).setValue(hotel)



    }


}




class Hotel(){
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0
    var cuisine : List<String>? = null
    constructor( id : Int , name : String, city : String, lat : Double , long : Double,
                 cuisine : List<String>
                ) : this(){
                    this.id = id
                    this.name =name
                    this.city = city
                    this.lat = lat
                    this.long = long
                   this.cuisine = cuisine
                }
      }



class CuisineList (){
    var itemNo : Int = 0
    var description : String = ""
    var price : Int = 0
    var cuisinetype : String = ""
    constructor(itemNo : Int ,description : String,price : Int, cuisinetype : String) : this(){
        this.description = description
        this.price = price
        this.cuisinetype =  cuisinetype
        this.itemNo = itemNo
    }
}
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
    var counter : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        rView = findViewById(R.id.rView)

        floatButton = findViewById(R.id.fabB)

        rView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database

        getHotelDetails()

//        addHotel()

    }

    override fun onResume() {
        super.onResume()
        getHotelDetails()
    }

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

                        val cuisintype = it.getStringArrayListExtra("cuisinetype") as List<String>
                        val itemno = it.getIntExtra("itemid",0)
                        val itemdescription = it.getStringExtra("itemdescription") ?: ""
                        val itemprice = it.getIntExtra("itemprice",500)

                        addHotel(hotelName,id,city,lat,long,cuisintype,itemno,itemdescription,itemprice)

                        Toast.makeText(this,"Received $hotelName & $city & $lat & $long" +
                                "& $cuisintype",
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
        val ref = db.getReference("HotelDB").child("HotelList")   // to go to HotelList node in db

        ref.addValueEventListener(object : ValueEventListener{  // adding a listener to this node for any data change
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AdminActivity","${snapshot.value}")
                val hotel = snapshot.getValue(HotelList::class.java)   // value in it will be list of hotel

                hotel.let {
                    rView.adapter = HotelListAdapter(it?.hotel)
                    // doing iteration over list of hotel
                    it?.hotel?.forEach {


                        Log.d("AdminActivity","hotel id : ${it.id} &" +
                                "hotel name : ${it.name}  & ${it.city}")
                    }
                }

                val hotelobj = snapshot.child("hotel")  //going to child node

                // doing iteration over the hotel list
                hotelobj.children.forEach {
                    val hotelList = it.getValue(Hotel::class.java)  // collecting value in Hotel Class


                    Log.d("AdminActivity","hotel id : ${hotelList?.id} &" +
                            "hotel name : ${hotelList?.name}  & ${hotelList?.city}")

                    val cuisineList = hotelList?.cuisine
                    cuisineList?.forEach {

                        Log.d("AdminActivity","type : ${it.type}")

                        val itemList = it.items
                        itemList?.forEach {
                            Log.d("AdminActivity","price : ${it.price} & " +
                                    "description : ${it.description}")
                        }

                    }


                }
//                val cuisineobj = hotelobj.child("cuisine")
//                cuisineobj.children.forEach {
//                    Log.d("AdminActivity","${cuisineobj}")
//                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addHotel(hotelName : String ,id : Int, city : String, lat : Double,
                         long : Double, cuisintype : List<String>,
                         itemno : Int , itemdescription : String, itemprice : Int)
      {


//        val menuitem = mutableListOf<Item>()
//        menuitem.add(0,Item("Puri Bhaji",150))
//
//        val menuitem2 = mutableListOf<Item>()
//        menuitem2.addAll(0, mutableListOf(Item("Pasta",450),Item("Noodles",350)))

        val menuitem3 = mutableListOf<Item>()
//        menuitem3.add(0,Item("Nachos",245))

          menuitem3.add(Item(itemdescription,itemprice))

//        val  cuisineList = mutableListOf<Cuisine>()
//        cuisineList.add(0, Cuisine("Indian",menuitem))
//        cuisineList.add(1, Cuisine("Mexican",menuitem3))

        val cuisineList2 = mutableListOf<Cuisine>()
//        cuisineList2.add(0, Cuisine("Italian",menuitem2))
          for( type in cuisintype){
              cuisineList2.add(Cuisine(type,menuitem3))
          }

        val hotelList = mutableListOf<Hotel>()
//        hotelList.add(0, Hotel(0,"Taj Hotel","Mumbai",80.5,90.6,cuisineList))
//        hotelList.add(1, Hotel(1,"Sahara Hotel","Lucknow",80.5,45.7,cuisineList2))
        hotelList.add(Hotel(id,hotelName,city,lat,long,cuisineList2))

        val listOfHotel = HotelList(hotelList)

        val ref = db.getReference("HotelDB")
        ref.child("HotelList").push().setValue(listOfHotel)

//      counter++

    }


}

class Item (){
    var description : String = ""
    var price : Int = 0
    constructor(description : String,price : Int) : this(){
     this.description = description
     this.price = price
    }
}

class Cuisine( ){
    var type : String = ""
    var items :  List<Item>? = null
    constructor(type : String,items : List<Item>) : this(){
       this.items = items
       this.type = type
    }

}
class Hotel(){
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0
    var cuisine : List<Cuisine>? = null
    constructor( id : Int , name : String, city : String, lat : Double , long : Double,
                cuisine: List<Cuisine>) : this(){
                    this.id = id
                    this.name =name
                    this.city = city
                    this.lat = lat
                    this.long = long
                    this.cuisine = cuisine
                }
}

class HotelList(){
    var hotel : List<Hotel>? = null
    constructor(hotel : List<Hotel>) : this(){
    this.hotel = hotel

    }
}
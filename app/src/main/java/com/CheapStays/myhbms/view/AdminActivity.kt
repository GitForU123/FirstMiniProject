package com.CheapStays.myhbms.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        rView = findViewById(R.id.rView)

        rView.layoutManager = LinearLayoutManager(this)

        db = Firebase.database

        addHotel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("get HotelsDetails")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.title){
            "get HotelsDetails" ->{
                getHotelDetails()
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun addHotel() {

//        val current = Weather(123466,35.6,77.0,999) // property of Weather
//        val current2 = Weather(222222,43.6,56.9,885)


//
//        val weatherlist = mutableListOf<Weather>()
//        weatherlist.add(0,Weather(556677,65.8,78.9,899)) // list of Weather item
//
//        val ref = db.getReference("CurrentDb")
//        ref.child("Current").child(current.dt.toString()).setValue(current)
//        ref.child("Current").child(current2.dt.toString()).setValue(current2)  // adding to same node
//
//        ref.child("hourly").setValue(weatherlist)
//
//        val timezone1 = "Asia/Kolkata"
//        ref.child("timezone").setValue(timezone1)
//
//        val currentWeather = Current(weatherlist,"Asia/Delhi",current)
//
//
//        ref.child("CurrentWeather").setValue(currentWeather)

        val menuitem = mutableListOf<Item>()
        menuitem.add(0,Item("Puri Bhaji",150))

        val menuitem2 = mutableListOf<Item>()
        menuitem2.addAll(0, mutableListOf(Item("Pasta",450),Item("Noodles",350)))

        val menuitem3 = mutableListOf<Item>()
        menuitem3.add(0,Item("Nachos",245))
        val  cuisineList = mutableListOf<Cuisine>()
        cuisineList.add(0, Cuisine("Indian",menuitem))
        cuisineList.add(1, Cuisine("Mexican",menuitem3))

        val cuisineList2 = mutableListOf<Cuisine>()
        cuisineList2.add(0, Cuisine("Italian",menuitem2))

        val hotelList = mutableListOf<Hotel>()
        hotelList.add(0, Hotel(0,"Taj Hotel","Mumbai",80.5,90.6,cuisineList))
        hotelList.add(1, Hotel(1,"Sahara Hotel","Lucknow",80.5,45.7,cuisineList2))

        val listOfHotel = HotelList(hotelList)

        val ref = db.getReference("HotelDB")
        ref.child("HotelList").setValue(listOfHotel)



    }
}

//data class Weather(val dt : Long ,val temp : Double, val wind_speed : Double, val pressure : Int)
//
//data class Current(val hourly : List<Weather>, val timezone : String, val current : Weather)



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
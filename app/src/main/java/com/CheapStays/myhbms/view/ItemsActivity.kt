package com.CheapStays.myhbms.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.CheapStays.myhbms.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_items.*

class ItemsActivity : AppCompatActivity() {
    var hotelid : Int = 0
    lateinit var db : FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        val i = intent
        hotelid = i.getIntExtra("hotelid",0)

        db = Firebase.database



    }

    private fun addItems(hotelid: Int) {

        val itemNo = itemNoE.text.toString().toInt()
        val itemdescription = itemdescriptionE.text.toString()
        val itemprice = itempriceE.text.toString().toInt()
        val cuisinetype = cuisinetypeEd.text.toString()

        val item = Item(itemNo,itemdescription,itemprice,cuisinetype)

        val ref = db.getReference("HotelDB").child("Hotel")
        ref.child("$hotelid").child("cuisinetype").child(cuisinetype).child("$itemNo").setValue(item)

    }

    fun saveClick(view: View) {

        addItems(hotelid)

        val intent = Intent(this,AdminActivity::class.java)
        startActivity(intent)
        finish()
    }
}
class Item (){
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
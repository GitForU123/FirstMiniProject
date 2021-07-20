package com.CheapStays.myhbms.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import com.CheapStays.myhbms.R
import kotlinx.android.synthetic.main.activity_hotel_details.*
import kotlinx.android.synthetic.main.hotel_item.*

class HotelDetailsActivity : AppCompatActivity() {
    var typeList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)


    }

    fun buttonClick(view: View) {
        when(view.id){
            R.id.saveB ->{
            val  hotelName = hotelnameE.text.toString()
            val id = hotelidE.text.toString().toInt()
            val cityName = citynameE.text.toString()
            val latitude = latE.text.toString().toDouble()
            val longitude = lonE.text.toString().toDouble()

            val cuisinetype = ArrayList(typeList)
            val itemid = cuisineitemNo.text.toString().toInt()
            val itemdescription = itemdescriptionE.text.toString()
            val itemprice = itempriceE.text.toString().toInt()

            val intent = Intent(this,AdminActivity::class.java)
            intent.putExtra("name",hotelName)
            intent.putExtra("id",id)
            intent.putExtra("city",cityName)
            intent.putExtra("lat",latitude)
            intent.putExtra("lon",longitude)

            intent.putStringArrayListExtra("cuisinetype",cuisinetype)
            intent.putExtra("itemid",itemid)
            intent.putExtra("itemdescription",itemdescription)
            intent.putExtra("itemprice",itemprice)

            setResult(RESULT_OK,intent)
                finish()
            }
            R.id.cancelB ->{
                finish()
            }
        }
    }

    fun cbClicked(view: View) {
        val cBox = view as CheckBox
        val type = cBox.text.toString()

        if(cBox.isChecked)
            typeList.add(type)
        else
            typeList.remove(type)
    cuisinetypeListE.setText("$typeList")
    }
}
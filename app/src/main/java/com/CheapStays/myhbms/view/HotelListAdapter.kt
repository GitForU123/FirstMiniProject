package com.CheapStays.myhbms.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import java.lang.StringBuilder

class HotelListAdapter(val hotelList : List<Hotel>?)  : RecyclerView.Adapter<HotelListAdapter.HotelHolder>(){

    class HotelHolder(view : View) : RecyclerView.ViewHolder(view){
        // this class holds all the view
        val hotelimage = view.findViewById<ImageView>(R.id.hoteLIV)
        val hotelName = view.findViewById<TextView>(R.id.nameT)
        val city = view.findViewById<TextView>(R.id.cityT)
        val cuisineType = view.findViewById<TextView>(R.id.cuisinetypeT)

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HotelListAdapter.HotelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_item,
            parent,false)
        return HotelHolder(view)
    }

    override fun onBindViewHolder(holder: HotelListAdapter.HotelHolder, position: Int) {
       val hotel = hotelList?.get(position)
        holder.hotelName.text = hotel?.name
        holder.city.text = hotel?.city
        var cuisineType = StringBuilder()
        val cuisineList = hotel?.cuisine
        cuisineList?.forEach {
            cuisineType.append(it.type).append(" ")
        }
        holder.cuisineType.text = cuisineType
    }

    override fun getItemCount() = hotelList!!.size
}
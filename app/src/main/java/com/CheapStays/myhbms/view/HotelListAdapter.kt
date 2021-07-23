package com.CheapStays.myhbms.view

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.bumptech.glide.Glide
import java.lang.StringBuilder

class HotelListAdapter(val hotelList : ArrayList<Hotel>)  : RecyclerView.Adapter<HotelListAdapter.HotelHolder>(){

    class HotelHolder(view : View) : RecyclerView.ViewHolder(view){
        // this class holds all the view
        val hotelimage = view.findViewById<ImageView>(R.id.hotIV)
        val hotelName = view.findViewById<TextView>(R.id.hotnameT)
        val city = view.findViewById<TextView>(R.id.hotcityT)
        val cuisineType = view.findViewById<TextView>(R.id.hotcuisineT)



    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HotelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotel_details,
            parent,false)
        return HotelHolder(view)
    }

    override fun onBindViewHolder(holder: HotelHolder, position: Int) {

        holder.itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu?.add("Add items & Image")?.setOnMenuItemClickListener {
               Log.d("Context","Clicked at postion $position & $it")
                // launch an activity to add items

                val intent = Intent(holder.itemView.context,ItemsActivity::class.java)
                intent.putExtra("hotelid",hotelList[position].id)
                holder.itemView.context.startActivity(intent)
                val activity = holder.itemView.context as Activity
                activity.finish()
                true
            }
        }

       val hotel = hotelList[position]
        holder.hotelName.text = hotel.name
        holder.city.text = hotel.city

        val imageurl = hotel.url
        Glide.with(holder.itemView)
            .load(imageurl)
            .into(holder.hotelimage)

        var cuisineType = StringBuilder()
        val cuisineList = hotel?.cuisine
        cuisineList?.forEach {
            cuisineType.append(it).append(" ")
        }
        holder.cuisineType.text = cuisineType
    }

    override fun getItemCount() = hotelList.size

}
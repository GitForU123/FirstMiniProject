package com.CheapStays.myhbms.view

import android.content.Intent
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import java.lang.StringBuilder

class HotelListAdapter(val hotelList : ArrayList<Hotel>)  : RecyclerView.Adapter<HotelListAdapter.HotelHolder>(){

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

        holder.itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu?.add("Add items")?.setOnMenuItemClickListener {
               Log.d("Context","Clicked at postion $position & $it")
                // launch an activity to add items

                val intent = Intent(holder.itemView.context,ItemsActivity::class.java)
                intent.putExtra("hotelid",hotelList[position].id)
                holder.itemView.context.startActivity(intent)
                true
            }
        }
       val hotel = hotelList[position]
        holder.hotelName.text = hotel.name
        holder.city.text = hotel.city

//        var cuisineType = StringBuilder()
//        val cuisineList = hotel?.cuisine
//        cuisineList?.forEach {
//            cuisineType.append(it.type).append(" ")
//        }
//        holder.cuisineType.text = cuisineType
    }

    override fun getItemCount() = hotelList.size

}
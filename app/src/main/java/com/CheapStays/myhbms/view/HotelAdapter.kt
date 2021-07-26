package com.CheapStays.myhbms.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.bumptech.glide.Glide
import java.lang.StringBuilder
import java.text.DecimalFormat

class HotelAdapter( val hotelList : ArrayList<HotelForUser>,val userloc : Location)  : RecyclerView.Adapter<HotelAdapter.HotelHolder>(){

    class HotelHolder(view : View) : RecyclerView.ViewHolder(view){
        // this class holds all the view
        val hotelimage = view.findViewById<ImageView>(R.id.hotelUIV)
        val hotelName = view.findViewById<TextView>(R.id.hotelnameUT)
        val city = view.findViewById<TextView>(R.id.cityUT)
        val cuisineType = view.findViewById<TextView>(R.id.cuisineUT)
        val distance = view.findViewById<TextView>(R.id.distanceT)



    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HotelHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hotellist_for_user,
            parent,false)
        return HotelHolder(view)
    }

    override fun onBindViewHolder(holder: HotelHolder, position: Int) {



        val hotel = hotelList[position]
        holder.hotelName.text = hotel.name
        holder.city.text = hotel.city

        val lat = hotel.lat
        val long = hotel.long

        // creating a location based on entered hotel latitude and longitude
        val locpoint = Location("hotelloc")
        locpoint.latitude = lat
        locpoint.longitude = long

        // getting distance between to location and converting into Kilometers
        val kmdistance =(userloc.distanceTo(locpoint))/1000

        val shortkm = DecimalFormat("#.##").format(kmdistance)
        holder.distance.text = shortkm


//        holder.itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
//            menu?.add("Go to hotel")?.setOnMenuItemClickListener {

        // pass value of hotel id here
        val hdFragment = HotelItemDetailsFragment()
        val hotelid = hotel.id.toString()
        val mybundle = Bundle()
        mybundle.putString("hotelid", hotelid)

        hdFragment.arguments = mybundle
                var navController : NavController? = null
                with(holder.itemView){
                    this.setOnClickListener {
                        navController = Navigation.findNavController(this)
                        navController!!.navigate(R.id.action_navigation_home_to_hotelItemDetailsFragment2,mybundle)
                    }
                }
//                Log.d("Context","Clicked at postion $position & $it")


//              findNavController().navigate(R.id.action_navigation_home_to_hotelItemDetailsFragment2,mybundle)

                //using nav controller go to HotelDetails Fragment

//
//                true
//            }
//        }

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
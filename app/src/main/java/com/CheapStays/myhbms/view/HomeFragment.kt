package com.CheapStays.myhbms.view

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment(){

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var db : FirebaseDatabase
    lateinit var rView : RecyclerView
    lateinit  var hotelList : ArrayList<HotelForUser>
    lateinit var mProgressDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Firebase.database     // getting Firebase database initialize here
        hotelList = arrayListOf()  // initializing hotelList
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showProgressDialog()
        // Start Location service to get user Location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val imageButton = view.findViewById<ImageButton>(R.id.imageButton)
        rView = view.findViewById(R.id.userRV)

        rView.layoutManager = LinearLayoutManager(context)
        getlocation()


        imageButton.setOnClickListener{
            // Navigating to maps Fragment
            findNavController().navigate(R.id.action_navigation_home_to_mapsFragment)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showProgressDialog() {
        mProgressDialog = context?.let { Dialog(it) }!!


        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.setCancelable(false)

        mProgressDialog.show()
    }

    private fun getlocation() {

        // to check if permission is there
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MapsFragment.LOCATION_REQUEST_CODE
                )
            }

        }
        activity?.let {
            // getting last location on device saved
            fusedLocationClient.lastLocation.addOnSuccessListener(it) { location ->
                if (location != null){
                    lastLocation = location
                    val lat = lastLocation.latitude
                    val long = lastLocation.longitude
                    // passing the lastlocation to get distance
                    getHotelList(lastLocation)
                    Log.d("Location","current location $lastLocation" +
                            "& lat $lat & long $long")
                }
            }
        }

    }

    private fun getHotelList(userloc : Location){
        val ref = db.getReference("HotelDB").child("Hotel")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hideProgressDialog()
                hotelList.clear()
                for (hotelSnapshot in snapshot.children) {

                    val hotel = hotelSnapshot.getValue(HotelForUser::class.java)

                    if (hotel != null) {
                        hotelList.add(hotel)
                    }

                }

                rView.adapter = HotelAdapter(hotelList,userloc)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }


}

class HotelForUser {
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0
    var cuisine : List<String>? = null
    var url : String = ""
}
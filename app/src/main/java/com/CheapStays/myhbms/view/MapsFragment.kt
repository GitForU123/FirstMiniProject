package com.CheapStays.myhbms.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.CheapStays.myhbms.HotelForMap
import com.CheapStays.myhbms.MapsActivity
import com.CheapStays.myhbms.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var db: FirebaseDatabase

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.database

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            val title =it.title ?: ""
            val snippet = it.snippet
//            Toast.makeText(context,"you are here $title", Toast.LENGTH_SHORT).show()
            Log.d("Map","title $title & city $snippet")

            //show hotel item details

            val bundle = Bundle()
            bundle.putString("hotelid",title)
            findNavController().navigate(R.id.action_mapsFragment_to_hotelItemDetailsFragment,bundle)
//            val transaction = supportFragmentManager.beginTransaction()
//            val hotelItemDetailFrag = HotelItemDetailsFragment.newInstance(it.title!!)
//            transaction.replace(R.id.mapL,hotelItemDetailFrag)
//            transaction.commit()
            true
        }




        val ref = db.getReference("HotelDB").child("Hotel")   // to go to Hotel node in db

        ref.addValueEventListener(object : ValueEventListener {  // adding a listener to this node for any data change
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("AdminActivity", "${snapshot.value}")

                for (hotelSnapshot in snapshot.children) {
                    Log.d("AdminActivity", "$hotelSnapshot")
                    val hotel =
                        hotelSnapshot.getValue(HotelForMapFrag::class.java)   // value in it will be details of hotel

                    if (hotel != null) {
                        val lat = hotel.lat
                        val long = hotel.long

                        val hotelLoc = LatLng(lat, long)
                        val op = MarkerOptions().position(hotelLoc).title("${hotel.id}")
                        op.snippet("hotelname: ${hotel.name} " +
                                "city: ${hotel.city}")
                        mMap.addMarker(op)



                        Log.d(
                            "AdminActivity", "hotel id : $lat &" +
                                    "hotel name : ${hotel.name}  & $long  "
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        setUpMap()
    }


    private fun setUpMap() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MapsActivity.LOCATION_REQUEST_CODE
                )
            }

        }
        activity?.let {
            fusedLocationClient.lastLocation.addOnSuccessListener(it) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                placeMarkerOnMap(currentLatLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 8f))
                }
            }
        }
    }

//    private fun placeMarkerOnMap(currentLatLng: LatLng) {
//        val lat = currentLatLng.latitude
//        val long = currentLatLng.longitude
//        val markerOptions = MarkerOptions().position(currentLatLng)
//        markerOptions.title("Your delivery here").snippet("your $lat & $long")
//        mMap.addMarker(markerOptions)
//    }

    override fun onMarkerClick(p0: Marker?) = false

//


}

class HotelForMapFrag(){
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0

    constructor( id : Int , name : String, city : String, lat : Double , long : Double,
    ) : this(){
        this.id = id
        this.name =name
        this.city = city
        this.lat = lat
        this.long = long
//                    this.cuisine = cuisine
    }
}
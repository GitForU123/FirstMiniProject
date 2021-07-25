package com.CheapStays.myhbms.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "hotelid"


/**
 * A simple [Fragment] subclass.
 * Use the [HotelItemDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotelItemDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var hotelid: String = ""

    lateinit var db : FirebaseDatabase
    lateinit var itemList : ArrayList<CuisineList>
    lateinit var recyclerView : RecyclerView

    lateinit var hotelname : TextView
    lateinit var hotelcity : TextView
    lateinit var hotelimage  : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            hotelid = arguments?.getString(ARG_PARAM1).toString()

        db = Firebase.database

        itemList = arrayListOf()


//        recyclerView.layoutManager = GridLayoutManager(context,2)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hotel_item_details, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        hotelname = view.findViewById<TextView>(R.id.hotelNameT)
        hotelcity = view.findViewById<TextView>(R.id.hotelCityT)
        hotelimage = view.findViewById(R.id.hotelimageIV)
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)


        getCuisineList(hotelid)

      getHotelDetails(hotelid)

            Toast.makeText(context,"hotel id received $hotelid",Toast.LENGTH_SHORT).show()


        super.onViewCreated(view, savedInstanceState)
    }

    private fun getHotelDetails(hotelid: String) {

        val ref = db.getReference("HotelDB").child("Hotel").child(hotelid)

        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Details", "${snapshot.value}")
                val hotel = snapshot.getValue(HotelDetails::class.java)
                if (hotel != null) {
                    hotelname.text = hotel.name
                    hotelcity.text = hotel.city


                    val imageurl = hotel.url
                    Glide.with(this@HotelItemDetailsFragment)
                        .load(imageurl)
                        .into(hotelimage)

                    Log.d("HotelDetails","hotel name ${hotel?.name} & ${hotel.city}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance(id: String) =
            HotelItemDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, id)

                }
            }
    }

    private fun getCuisineList(hotelid : String){

        val ref = db.getReference("HotelDB").child("Hotel").child(hotelid).child("cuisinetype")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                Log.d("Cuisine", "${snapshot.value}")
                for (list in snapshot.children){
                    Log.d("Cuisine", "$list")   // list of cuisinetype
                    for(cuisineList in list.children)
                    {
                        Log.d("Cuisine", "$cuisineList")   // list of items
                        val cuisineValue = cuisineList.getValue(CuisineList::class.java)
                        Log.d("Cuisine", "${cuisineValue?.cuisinetype}  & ${cuisineValue?.itemNo} &" +
                                "${cuisineValue?.description} & ${cuisineValue?.price}")


                        itemList.add(cuisineValue!!)
                    }


                }

            recyclerView.adapter = ItemDetailsAdapter(itemList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
    }

}
class CuisineList (){
    var itemNo : Int = 0
    var description : String = ""
    var price : Int = 0
    var cuisinetype : String = ""
    var url : String = ""
    constructor(itemNo : Int ,description : String,price : Int, cuisinetype : String,
    url : String) : this(){
        this.description = description
        this.price = price
        this.cuisinetype =  cuisinetype
        this.itemNo = itemNo
        this.url = url
    }
}

class HotelDetails(){
    var id : Int = 0
    var name : String = ""
    var city : String = ""
    var lat : Double = 0.0
    var long : Double =0.0
    var cuisine : List<String>? = null
    var url : String = ""
    constructor( id : Int , name : String, city : String, lat : Double , long : Double,
                 cuisine : List<String>, url : String
    ) : this(){
        this.id = id
        this.name =name
        this.city = city
        this.lat = lat
        this.long = long
        this.cuisine = cuisine
        this.url = url
    }
}


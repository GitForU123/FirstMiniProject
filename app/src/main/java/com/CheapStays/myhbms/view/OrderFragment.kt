package com.CheapStays.myhbms.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var db : FirebaseDatabase
    lateinit var auth : FirebaseAuth
    var currentuserid : String ? = ""
    lateinit var orderRV : RecyclerView
    lateinit var totalpriceText : TextView
    var totalprice : Int = 0
    lateinit var orderList : ArrayList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Firebase.database

        currentuserid = Firebase.auth.currentUser?.uid
        orderList = arrayListOf()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        totalpriceText = view.findViewById<TextView>(R.id.totalpriceT)

        orderRV = view.findViewById(R.id.orderRV)
        orderRV.layoutManager = LinearLayoutManager(context)

        getOrder()


        super.onViewCreated(view, savedInstanceState)
    }

    private fun getOrder() {
        val ref = db.getReference("OrderDB").child("Order").child("$currentuserid")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (cartsnapshot in snapshot.children){
                    val order = cartsnapshot.getValue(Order::class.java)

                    var price = order?.itemprice?.toInt()
                    var dbprice = (price?.times(order?.count!!))
                    if (dbprice != null) {
                        totalprice += dbprice
                    }
                    if (order != null) {
                        orderList.add(order)
                    }
                }
                orderRV.adapter = OrderAdapter(orderList)
                totalpriceText.text = totalprice.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }
}
class Order(){
    var itemname : String? = ""
    var itemprice : String? = ""
    var count : Int = 1
    constructor(itemname: String?,itemprice : String?,count : Int): this(){
        this.itemname = itemname
        this.itemprice = itemprice
        this.count = count

    }

}
package com.CheapStays.myhbms.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "name"
private const val ARG_PARAM2 = "price"


class CartFragment : Fragment() {
    lateinit var db : FirebaseDatabase
    lateinit var cartRV : RecyclerView
    lateinit var cartList : ArrayList<Cart>
    private var itemname: String? = null
    private var price: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Firebase.database

        cartList = arrayListOf()
        arguments?.let {
            itemname = it.getString(ARG_PARAM1)
            price = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var count : Int = 1

        cartRV = view.findViewById(R.id.cartRV)
        cartRV.layoutManager = LinearLayoutManager(context)

        Toast.makeText(context,"itemname $itemname & price $price",Toast.LENGTH_SHORT).show()


            addToCart(itemname,price,count)


        getCart()

        // place order handle
        val orderButton = view.findViewById<Button>(R.id.orderB)

        orderButton.setOnClickListener {
            // create Orderdatabase and store the value picked
            val ref = db.getReference("OrderDB").child("Order")

            ref.child("currentuserid").setValue(cartList)

            // delete the CartDB here
            val ref2 = db.getReference("CartDB")
            ref2.removeValue()

            findNavController().navigate(R.id.action_navigation_cart_to_navigation_order)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun getCart() {
        val ref = db.getReference("CartDB").child("Cart").child("currentuserid")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cartList.clear()
                for (cartsnapshot in snapshot.children){
                    val cart = cartsnapshot.getValue(Cart::class.java)

                    if (cart != null) {
                        cartList.add(cart)
                    }
                }
                cartRV.adapter = CartAdapter(cartList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addToCart(itemname: String?, price: String?, count : Int) {

        val ref = db.getReference("CartDB").child("Cart")
        val cart = Cart(itemname,price,count)

        ref.child("currentuserid").child("$itemname").setValue(cart)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
class Cart(){
    var itemname : String? = ""
    var itemprice : String? = "200"
    var count : Int = 1
    constructor(itemname: String?,itemprice : String?,count : Int): this(){
       this.itemname = itemname
       this.itemprice = itemprice
       this.count = count

    }

}
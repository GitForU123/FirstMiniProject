package com.CheapStays.myhbms.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CartAdapter (val cartList : ArrayList<Cart>) : RecyclerView.Adapter<CartAdapter.CartHolder>() {

    var db : FirebaseDatabase = Firebase.database
    class CartHolder(view : View) : RecyclerView.ViewHolder(view){

      val cartitemname = view.findViewById<TextView>(R.id.cartitemnameT)
      val cartitemprice = view.findViewById<TextView>(R.id.cartitempriceT)
      val itemquantity = view.findViewById<TextView>(R.id.item_quantity_tv)
      val plushButton = view.findViewById<ImageView>(R.id.increase_item_quantity_iv)
      val minusButton = view.findViewById<ImageView>(R.id.decrease_item_quantity_iv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.cart_details,parent,false)
        return CartHolder(view)
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val cart = cartList[position]

        val item = cart.itemname
        val count =  cart.count
        val price = cart.itemprice
        val newprice = price?.toInt()
        holder.cartitemname.text = item
        holder.cartitemprice.text = (newprice?.times(count)).toString()
        holder.itemquantity.text =count.toString()

        holder.plushButton.setOnClickListener {
            // call the the add on same node
            updatecart(item,count)
            Toast.makeText(holder.itemView.context," + button clicked",Toast.LENGTH_SHORT).show()
        }

        holder.minusButton.setOnClickListener {
            // remove the item count by 1 if count 1 delete it
            decreasecart(item,count)
            Toast.makeText(holder.itemView.context," - button clicked",Toast.LENGTH_SHORT).show()
        }
    }

    private fun decreasecart(item: String?, count: Int) {
        var newcount = count
        newcount--
        if(newcount>=1){
            val ref = db.getReference("CartDB").child("Cart")
                .child("currentuserid").child("$item")

            ref.child("count").setValue(newcount)
            ref.child("itemprice")
        }else{
            val ref = db.getReference("CartDB").child("Cart")
                .child("currentuserid").child("$item")
            ref.removeValue()
        }


    }

    private fun updatecart(item: String?,count : Int) {
        // go to cart node having same name and update count by 1
        var newcount = count
        newcount++
        val ref = db.getReference("CartDB").child("Cart")
            .child("currentuserid").child("$item")

        ref.child("count").setValue(newcount)
        ref.child("itemprice")

    }

    override fun getItemCount() = cartList.size
}
package com.CheapStays.myhbms.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R

class OrderAdapter(val orderList : ArrayList<Order>) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {
    class OrderHolder(view : View) : RecyclerView.ViewHolder(view){
     val ordername = view.findViewById<TextView>(R.id.ordernameT)
     val ordercount = view.findViewById<TextView>(R.id.ordercountT)
     val orderprice = view.findViewById<TextView>(R.id.orderpriceT)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.order_details,parent,false)
        return OrderHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
       val order = orderList[position]

        val item = order.itemname
        val count =  order.count
        val price = order.itemprice
        val newprice = price?.toInt()
        holder.ordername.text = item
        holder.orderprice.text = (newprice?.times(count)).toString()
        holder.ordercount.text = order.count.toString()
    }

    override fun getItemCount() = orderList.size
}
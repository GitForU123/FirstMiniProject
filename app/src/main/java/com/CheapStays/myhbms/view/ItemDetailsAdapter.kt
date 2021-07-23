package com.CheapStays.myhbms.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.CheapStays.myhbms.R
import com.bumptech.glide.Glide

class ItemDetailsAdapter(val itemList : ArrayList<CuisineList>)  : RecyclerView.Adapter<ItemDetailsAdapter.ItemHolder>() {

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        // this class holds all the view
        val itemname = view.findViewById<TextView>(R.id.itemnameT)
        val itemtype = view.findViewById<TextView>(R.id.cuisineT)
        val itemprice = view.findViewById<TextView>(R.id.itempriceT)
        val itemimage = view.findViewById<ImageView>(R.id.itemIV)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemDetailsAdapter.ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_details,
            parent, false
        )
        return ItemHolder(view)
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

     val item = itemList[position]
        holder.itemname.text = item.description
        holder.itemtype.text = item.cuisinetype
        holder.itemprice.text = item.price.toString()

        val imageurl = item.url
        Glide.with(holder.itemView)
            .load(imageurl)
            .into(holder.itemimage)


    }

    override fun getItemCount() = itemList.size

}
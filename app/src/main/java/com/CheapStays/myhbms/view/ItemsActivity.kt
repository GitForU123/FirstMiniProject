package com.CheapStays.myhbms.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.CheapStays.myhbms.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_hotel_details.*
import kotlinx.android.synthetic.main.activity_items.*

//var itemcounter : Int = 1
class ItemsActivity : AppCompatActivity() {

    var hotelid : Int = 0
    lateinit var db : FirebaseDatabase
    lateinit var uri : Uri
    var itemImageURL : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

//        itemNoE.setText("$itemcounter")

        val i = intent
        hotelid = i.getIntExtra("hotelid",0)

        db = Firebase.database

        val itemimage = findViewById<ImageView>(R.id.itemimageIV)

        itemimage.setOnClickListener{
            Log.d("Image","image clicked")

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type ="image/*"
                startActivityForResult(it,0)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 0){
            when(resultCode){
                RESULT_OK ->{
                    uri = data?.data!! //this data refer to data of our intent

                    itemimageIV.setImageURI(uri)

                    val imageurl = uri.lastPathSegment
                    Log.d("Image","From gallery url $imageurl")

                    // do the upload here
                    uploadimage(uri)
                }
                RESULT_CANCELED ->{
                    // got no image to upload
                    Toast.makeText(this,"no image selected", Toast.LENGTH_SHORT).show()
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadimage(uri: Uri) {
        val imageName = uri.lastPathSegment.toString()

        val storageReference = FirebaseStorage.getInstance().reference.child("image/$imageName.jpg")

//        val imageURL = storageReference.downloadUrl

        storageReference.putFile(uri).addOnSuccessListener {
            it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                itemImageURL = it.toString()

                // here add a progress bar when complete let user interact

                Toast.makeText(this,"Image Successfully Uploaded",Toast.LENGTH_LONG).show()
            }?.addOnFailureListener{
                Toast.makeText(this,"Image Upload Failed",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun addItems(hotelid: Int) {

        val itemNo = itemNoE.text.toString().toInt()
        val itemdescription = itemdescriptionE.text.toString()
        val itemprice = itempriceE.text.toString().toInt()
        val cuisinetype = cuisinetypeEd.text.toString()


        val item = Item(itemNo,itemdescription,itemprice,cuisinetype,itemImageURL)

        val ref = db.getReference("HotelDB").child("Hotel")
        ref.child("$hotelid").child("cuisinetype").child(cuisinetype).child("$itemNo").setValue(item)

    }

    fun saveClick(view: View) {

        addItems(hotelid)

        val intent = Intent(this,AdminActivity::class.java)
        startActivity(intent)

        // increase counter here
//        itemcounter++

        finish()
    }
}
class Item (){
    var itemNo : Int = 0
    var description : String = ""
    var price : Int = 0
    var cuisinetype : String = ""
    var url : String = ""
    constructor(itemNo : Int ,description : String,price : Int, cuisinetype : String, url : String) : this(){
        this.description = description
        this.price = price
        this.cuisinetype =  cuisinetype
        this.itemNo = itemNo
        this.url = url
    }
}
package com.CheapStays.myhbms.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import com.CheapStays.myhbms.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_hotel_details.*
import kotlinx.android.synthetic.main.hotel_item.*

var idCouter : Int = 3
class HotelDetailsActivity : AppCompatActivity() {
    lateinit var uri : Uri
    var typeList = mutableListOf<String>()
    lateinit var db : FirebaseDatabase
    var hotelimageURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel_details)

//        hotelidE.setText("$idCouter")

        db = Firebase.database

        val hotelimage = findViewById<ImageView>(R.id.hoteliv)

        hotelimage.setOnClickListener{
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
                    // received image successfully so upload it to storage
                    uri = data?.data!! //this data refer to data of our intent

                    // image = view?.findViewById<ImageView>(R.id.ivPhoto)!!
                    hoteliv.setImageURI(uri)

                    val imageurl = uri.lastPathSegment
                    Log.d("Image","From gallery url $imageurl")

                    uploadImage(uri)


                }
                RESULT_CANCELED ->{
                    // got no image to upload
                    Toast.makeText(this,"no image selected", Toast.LENGTH_SHORT).show()
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun buttonClick(view: View) {
        when(view.id){
            R.id.saveB ->{

             //increase id counter
//             idCouter++
            val  hotelName = hotelnameE.text.toString()
            val id = hotelidE.text.toString().toInt()
            val cityName = citynameE.text.toString()
            val latitude = latE.text.toString().toDouble()
            val longitude = lonE.text.toString().toDouble()


                val cuisinetype = ArrayList(typeList)


            val intent = Intent(this,AdminActivity::class.java)
//
                addHotel(hotelName,id,cityName,latitude,longitude,cuisinetype,hotelimageURL)
                startActivity(intent)
                finish()


            }
            R.id.cancelB ->{
                finish()
            }
        }
    }

    private fun uploadImage( uri: Uri){

        val imageName = uri.lastPathSegment.toString()
        Log.d("AddFragment","imageName : $imageName")
        val storageReference = FirebaseStorage.getInstance().reference.child("image/$imageName.jpg")

        val imageURL = storageReference.downloadUrl

        storageReference.putFile(uri).addOnSuccessListener {
            it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                hotelimageURL = it.toString()

                Log.d("Image","Image For Storage URL $hotelimageURL")

                Toast.makeText(this,"Image Successfully Uploaded",Toast.LENGTH_LONG).show()
            }?.addOnFailureListener{
                Toast.makeText(this,"Image Upload Failed",Toast.LENGTH_LONG).show()
            }
        }

    }

    fun cbClicked(view: View) {
        val cBox = view as CheckBox
        val type = cBox.text.toString()

        if(cBox.isChecked)
            typeList.add(type)
        else
            typeList.remove(type)
    cuisinetypeListE.setText("$typeList")
    }


    private fun addHotel(hotelName : String ,id : Int, city : String, lat : Double,
                         long : Double, cuisinetype : List<String>, url : String)
    {
        val cuisineList = mutableListOf<String>()
        for( type in cuisinetype){
            cuisineList.add(type)
        }

        val hotel = Hotel(id,hotelName,city,lat,long,cuisineList,url)

        val ref = db.getReference("HotelDB")
        ref.child("Hotel").child(id.toString()).setValue(hotel)



    }
}

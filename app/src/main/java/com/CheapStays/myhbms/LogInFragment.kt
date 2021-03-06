package com.CheapStays.myhbms

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.CheapStays.myhbms.view.AdminActivity
import com.CheapStays.myhbms.view.UserHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "email"


/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private var email: String? = null
    private var password: String? = null
    lateinit var emailText: EditText
    lateinit var passText : EditText
    lateinit var logInButton : Button
    lateinit var auth : FirebaseAuth

    // reference to interface presenter

    var userid : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // intialize authentication db
        auth = Firebase.auth

        arguments?.let {
            email = it.getString(ARG_PARAM1)


            // init


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        emailText = view.findViewById(R.id.emailE)
        passText = view.findViewById(R.id.passE)
        logInButton = view.findViewById(R.id.logInB)

        emailText.setText(email)


        logInButton.setOnClickListener {
            val logemail = emailText.text.toString()
            val logpass = passText.text.toString()

            if(logemail.isNotEmpty()){
                activity?.let {
                    auth.signInWithEmailAndPassword(logemail,logpass).addOnCompleteListener(it){
                        if(it.isSuccessful){
                            userid = auth.currentUser?.uid!!
                            checkAdmin(userid)
                            Toast.makeText(context,"Succesfully signed in",Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context,"Authentication failed",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Toast.makeText(context,"pls put a valid email",Toast.LENGTH_SHORT).show()
            }




            passText.setText("")

        }


    }

    private fun checkAdmin(userid: String){
        if(userid == "FQq0vKB4MMNxlncS62Y6GQWf1iD3"){
            val intent = Intent(context,AdminActivity::class.java)
            startActivity(intent)
        }else{
            // launch the user page
            val intent = Intent(context, UserHomeActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(email: String?) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, email)

                }
            }
    }


}
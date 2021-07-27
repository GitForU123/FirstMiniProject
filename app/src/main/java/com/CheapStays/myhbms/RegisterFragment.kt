package com.CheapStays.myhbms

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "emailR"
private const val ARG_PARAM2 = "passR"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment(){
    // TODO: Rename and change types of parameters
    private var Remail: String? = null
    private var Rpass: String? = null
    lateinit var emailEditText: EditText
    lateinit var passEditText: EditText
    lateinit var registerButton : Button
    lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        arguments?.let {
            Remail = it.getString(ARG_PARAM1)
            Rpass = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init of presenter
//        presenter = CredentialPresenter(this)

        // get the id of text here

        emailEditText = view.findViewById(R.id.emailR)
        passEditText = view.findViewById(R.id.passR)

        registerButton = view.findViewById(R.id.registerB)
        // assign to Remail and Rpass
        // send that data Only Email data to LogIn Fragment
        registerButton.setOnClickListener {

            Remail = emailEditText.text.toString()
            Rpass = passEditText.text.toString()

            val registercode = onRegister(Remail!!, Rpass!!)

            // accessing method of presenter to
//            val logInCode = presenter.onLogin(Remail!!,Rpass!!)
            // on back press set the password field empty
           passEditText.setText("")

            when(registercode){
                0 -> {onRegisterError("Pls put an email")}
                1 -> {onRegisterError("Put a valid mail")}
                2 -> {onRegisterError("min password length is 6")}
                -1 ->{

                    val accountResult = auth.createUserWithEmailAndPassword(Remail!!, Rpass!!)
                    activity?.let {
                        accountResult.addOnCompleteListener(it) {
                            if(it.isSuccessful){
                                Log.d("Register","Success fully registered with mail $Remail")
                                val logInFrag =LogInFragment.newInstance(Remail)
                                // replace fragment
                                activity?.supportFragmentManager
                                    ?.beginTransaction()
                                    ?.replace(R.id.mapL, logInFrag)
                                    ?.addToBackStack(null)
                                    ?.commit()

                            }else{
                                Log.d("Register","Registration failed")
                                Toast.makeText(context,"Already a user ",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }



                    onRegisterSuccess("Registration Successfull")

                }
            }





        }

    }

    private fun onRegister(remail: String, rpass: String) : Int{
        return if(remail.isEmpty()) {
            0
        } else if(!Patterns.EMAIL_ADDRESS.matcher(remail).matches()) {
            1
        } else if(rpass.length <6) {
            2
        } else {
            -1
        }
    }

    fun onRegisterSuccess(msg : String) {

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun onRegisterError(msg : String) {

        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()

    }


}
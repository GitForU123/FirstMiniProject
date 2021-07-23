package com.CheapStays.myhbms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.CheapStays.myhbms.presenter.CredentialPresenter
import com.CheapStays.myhbms.presenter.IPresenter
import com.CheapStays.myhbms.view.ILogInView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "emailR"
private const val ARG_PARAM2 = "passR"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment(),ILogInView{
    // TODO: Rename and change types of parameters
    private var Remail: String? = null
    private var Rpass: String? = null
    lateinit var emailEditText: EditText
    lateinit var passEditText: EditText
    lateinit var registerButton : Button

    // presenter reference here
    lateinit var presenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        presenter = CredentialPresenter(this)

        // get the id of text here

        emailEditText = view.findViewById(R.id.emailR)
        passEditText = view.findViewById(R.id.passR)

        registerButton = view.findViewById(R.id.registerB)
        // assign to Remail and Rpass
        // send that data Only Email data to LogIn Fragment
        registerButton.setOnClickListener {

            Remail = emailEditText.text.toString()
            Rpass = passEditText.text.toString()

            // accessing method of presenter to
            val logInCode = presenter.onLogin(Remail!!,Rpass!!)
            // on back press set the password field empty
           passEditText.setText("")

            when(logInCode){
                0 -> {onLogInError("Pls put an email")}
                1 -> {onLogInError("Put a valid mail")}
                2 -> {onLogInError("min password length is 8")}
                -1 ->{
                    val logInFrag =LogInFragment.newInstance(Remail,Rpass)
                    // replace fragment
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.mapL, logInFrag)
                        ?.addToBackStack(null)
                        ?.commit()


                    onLogInSuccess("LogIn Successfull")

                }
            }





        }

    }

    override  fun onLogInSuccess(msg : String) {
        // method of view
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onLogInError(msg : String) {
       // method of View
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()

    }


}
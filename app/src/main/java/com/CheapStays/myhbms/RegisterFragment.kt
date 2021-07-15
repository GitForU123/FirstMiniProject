package com.CheapStays.myhbms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "Remail"
private const val ARG_PARAM2 = "Rpass"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var Remail: String? =" abc@gamail.com"
    private var Rpass: String? = "12345"
    lateinit var registerButton : Button

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
        // get the id of text here

        registerButton = view.findViewById(R.id.registerB)
        // assign to Remail and Rpass
        // send that data Only Email data to LogIn Fragment
        registerButton.setOnClickListener {
            val logInFrag =LogInFragment.newInstance(Remail,Rpass)

            // replace fragment
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.parentL, logInFrag)
                ?.addToBackStack(null)
                ?.commit()
        }

    }
}
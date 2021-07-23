package com.CheapStays.myhbms

import android.content.Intent
import android.os.Bundle
import android.security.keystore.UserNotAuthenticatedException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.CheapStays.myhbms.presenter.AdminPresenter
import com.CheapStays.myhbms.presenter.CredentialPresenter
import com.CheapStays.myhbms.presenter.IPresenter
import com.CheapStays.myhbms.presenter.LogInpresenter
import com.CheapStays.myhbms.view.AdminActivity
import com.CheapStays.myhbms.view.ILogInView
import com.CheapStays.myhbms.view.UserActivity
import com.CheapStays.myhbms.view.UserHomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "email"
private const val ARG_PARAM2 = "pass"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters

    var isAdmin = false
    private var email: String? = null
    private var password: String? = null
    lateinit var emailText: EditText
    lateinit var passText : EditText
    lateinit var logInButton : Button

    // reference to interface presenter
    lateinit var presenter : LogInpresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString(ARG_PARAM1)
            password = it.getString(ARG_PARAM2)

            // init
            presenter = AdminPresenter(this)

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

        Toast.makeText(context,"received email: $email" +
                "password : $password",Toast.LENGTH_SHORT).show()
        logInButton.setOnClickListener {
            val logemail = emailText.text.toString()
            val logpass = passText.text.toString()
            isAdmin = presenter.checkAdmin(logemail,logpass)  // Using method of LogInPresenter
            passText.setText("")
            if(isAdmin){
                // launch the admin page
                val intent = Intent(context,AdminActivity::class.java)
                startActivity(intent)
                // close the fragment

            }else{
                // launch the user page
                val intent = Intent(context, UserHomeActivity::class.java)
                startActivity(intent)
            }
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
        fun newInstance(email: String?, pass: String?) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, email)
                    putString(ARG_PARAM2, pass)
                }
            }
    }


}
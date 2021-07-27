package com.CheapStays.myhbms.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import com.CheapStays.myhbms.LogInFragment
import com.CheapStays.myhbms.R
import com.CheapStays.myhbms.RegisterFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IntroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IntroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btn_sign_up_intro : Button
    lateinit var btn_sign_in_intro : Button
    lateinit var window : Window

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window = activity?.window !!
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btn_sign_up_intro = view.findViewById(R.id.btn_sign_up_intro)
        btn_sign_in_intro = view.findViewById(R.id.btn_sign_in_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Launch the sign up screen.
        btn_sign_up_intro.setOnClickListener {

           // do transaction to go to register page
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val registerfrag = RegisterFragment()
            transaction?.replace(R.id.mapL,registerfrag)
            transaction?.commit()
        }
        // Launch the sign in screen.
        btn_sign_in_intro.setOnClickListener {

           // go to sign in page
            val logInFrag = LogInFragment()
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.mapL, logInFrag)
                ?.addToBackStack(null)
                ?.commit()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment IntroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
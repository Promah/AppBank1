package com.example.appbank1


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.view.*



class LoginFragment : Fragment() {

    private var model: Communicator? = null
    private val serverResponseObserver = Observer<ServerResponseData?> { t ->
        t?.let {
            if (it.isSuccessful){
                findNavController().navigate(R.id.action_loginFragment_to_userCabinetFragment)
            }else{
                //show err
                Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)

        //autofill login field
        model!!.deviceSimOneNumber()?.let {
            v.inputUserLogin.setText(it)
        }

        v.buttonSignIn.setOnClickListener {
            model!!.login(v.inputUserLogin.text.toString(), v.inputUserPassword.text.toString())
        }

        v.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        model!!.getServerResponse().observe(activity!!, serverResponseObserver)

        return v
    }

    override fun onPause() {
        super.onPause()
        model!!.getServerResponse().removeObserver(serverResponseObserver)
    }

}

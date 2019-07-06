package com.example.appbank1


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_register.view.*


class RegisterFragment : Fragment() {

    private var model: Communicator? = null

    private val serverResponseObserver = Observer<ServerResponseData?> { t ->
        t?.let {
            if (it.isSuccessful){
                Toast.makeText(context,"registration is complete", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_userCabinetFragment)
            }else{
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
        val v = inflater.inflate(R.layout.fragment_register, container, false)

        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)

        //autofill login field
        model!!.deviceSimOneNumber()?.let {
            println("deviceSimOneNumber is $it")
            v.inputRegisterLogin.setText(it)
        }

        v.buttonRegister.setOnClickListener {
            if (v.inputRegisterPassword.text.toString().equals(v.inputRegisterPasswordDouble.text.toString())){
                model!!.register(v.inputRegisterLogin.text.toString(), v.inputRegisterPassword.text.toString())
            }else{
                Toast.makeText(context, "Passwords mast match!",Toast.LENGTH_SHORT).show()
            }
        }

        model!!.getServerResponse().observe(activity!!, serverResponseObserver)


        return v
    }

    override fun onPause() {
        super.onPause()
        model!!.getServerResponse().removeObserver(serverResponseObserver)
    }

}

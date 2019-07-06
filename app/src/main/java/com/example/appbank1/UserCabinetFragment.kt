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
import kotlinx.android.synthetic.main.fragment_user_cabinet.*
import kotlinx.android.synthetic.main.fragment_user_cabinet.view.*
import java.lang.Exception
import java.util.*


class UserCabinetFragment : Fragment() {

    private var model: Communicator? = null
    private val serverResponseObserver = Observer<ServerResponseData?> { t ->
        t?.let {
            if (it.isSuccessful){
                if (it.header.equals(ServerResponseData.HEADER_BALANCE))
                    textViewUserCabinetUserBalance?.setText("You`r money balance: ${it.data.toString()}")
                else if (it.header.equals(ServerResponseData.HEADER_SEND_MONEY)){
                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_LONG).show()
                    model?.getUserBalance()
                }
                else{
                    //pass
                }
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
        val v = inflater.inflate(R.layout.fragment_user_cabinet, container, false)
        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)

        model!!.getServerResponse().observe(activity!!, serverResponseObserver)

        v.buttonSendMoney.setOnClickListener {
            val userReciever = v.inputUserCabinetUserRetrieverLogin.text.toString()
            val moneyAmount = v.inputUserCabinetMoneyAmount.text.toString().toIntOrNull()
            if (userReciever.isNotEmpty() && moneyAmount != null && moneyAmount > 0){
                model?.sendMoney(userReciever,moneyAmount)
            }
        }

        v.buttonViewTransactionHistory.setOnClickListener {
            findNavController().navigate(R.id.action_userCabinetFragment_to_transactionHistoryFragment)
        }

        model?.getUserBalance()

        return v
    }

    override fun onPause() {
        super.onPause()
        model!!.getServerResponse().removeObserver(serverResponseObserver)
    }

}

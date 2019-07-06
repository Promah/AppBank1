package com.example.appbank1


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_transaction_history.*
import kotlinx.android.synthetic.main.fragment_transaction_history.view.*


class TransactionHistoryFragment : Fragment() {

    private var model: Communicator? = null
    private var RVAdapter = TransactionRecycleAdapter()
    var asdas = mutableListOf<TransactionRecycleData>()
    private val serverResponseObserver = Observer<ServerResponseData?> { t ->
        t?.let {
            if (it.isSuccessful){
//                Toast.makeText(context, "isSuccessful", Toast.LENGTH_SHORT).show()
                if (it.header.equals(ServerResponseData.HEADER_TRANSACTION_HISTORY)){
                    RVAdapter.setItemsList(it.data as MutableList<TransactionRecycleData>)
                    transactionHistoryRecycler.adapter = RVAdapter
                    progressBar.visibility = ProgressBar.GONE
                }

                Toast.makeText(context, "isSuccessful", Toast.LENGTH_LONG).show()
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
        val v = inflater.inflate(R.layout.fragment_transaction_history, container, false)
        model = ViewModelProviders.of(activity!!).get(Communicator::class.java)

        model!!.getServerResponse().observe(activity!!, serverResponseObserver)

        v.transactionHistoryRecycler.layoutManager = LinearLayoutManager(context)
        v.transactionHistoryRecycler.adapter = RVAdapter

        model!!.getItemsHistory()

        return v
    }

    override fun onPause() {
        super.onPause()
        model!!.getServerResponse().removeObserver(serverResponseObserver)
    }

}

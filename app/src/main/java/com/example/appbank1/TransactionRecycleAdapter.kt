package com.example.appbank1

import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.transaction_list_item.view.*
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate
import java.util.*

class TransactionRecycleAdapter : RecyclerView.Adapter<TransactionHolder>() {

    private var itemsList = mutableListOf<TransactionRecycleData>()

    fun setItemsList(newItemsList: MutableList<TransactionRecycleData>){
        itemsList = newItemsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
//        val layout = LayoutInflater.from().inflate(R.layout.transaction_list_item, parent, false)
        return TransactionHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaction_list_item, parent, false))
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        holder.onBind(
            itemsList[position].transactionFromText,
            itemsList[position].transactionToText,
            itemsList[position].transactionAmountText,
            itemsList[position].transactionTimeText
            )
    }


}
class TransactionHolder(v: View) : RecyclerView.ViewHolder(v){
    fun onBind(
        transactionFromText : String,
        transactionToText : String,
        transactionAmountText : String,
        transactionTimeText: String
    ){
        itemView.transactionFrom.text = "From:"+transactionFromText
        itemView.transactionTo.text = "To:"+transactionToText
        itemView.transactionAmount.text = "Amount:"+transactionAmountText

        //transform PHP server "time()" to JVM time
        val dateMillis = transactionTimeText.toLong()*1000+10800000
//        val sdf = SimpleDateFormat("MMM dd,yyyy HH:mm")
        val sdf = SimpleDateFormat("yyyy MM dd  HH:mm")
        val transactionTimeAsText = sdf.format(Date(dateMillis))


        itemView.transactionTime.text = "Time:"+transactionTimeAsText
    }
}
package com.example.appbank1

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.example.appbank1.logic.DeviceInfo
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class Communicator : ViewModel(){

    private val serverResponse = MutableLiveData<ServerResponseData?>()
    fun getServerResponse() : LiveData<ServerResponseData?> = serverResponse

    private var deviceInfo : DeviceInfo? = null
    private val PREFS_FILENAME = "com.exemlpe.appbank1.prefs"
    private var sharedPreferences : SharedPreferences? = null

    companion object{
        private val client = OkHttpClient()
        private val URL_LOGIN = "http://avtomir.it-hb.org/avtomir_api/login/?"
        private val URL_GETUSERBALANCE = "http://avtomir.it-hb.org/avtomir_api/getUserBalance?"
        private val URL_REGISTER = "http://avtomir.it-hb.org/avtomir_api/register?"
        private val URL_SEND_MONEY = "http://avtomir.it-hb.org/avtomir_api/send_money?"
        private val URL_TRANSACTION_HISTORY = "http://avtomir.it-hb.org/avtomir_api/getUserTransactionHistory/?"

    }



    fun initSharedPreferences(application : AppCompatActivity){
        sharedPreferences = application.getSharedPreferences(PREFS_FILENAME, 0)
    }

    private fun saveToken(token:String){
        sharedPreferences?.edit().also {
            it?.putString("appbank1.token", token)
            it?.apply()
        }
    }
    private fun getSavedToken():String?{
        val token = sharedPreferences?.getString("appbank1.token", null)
        return token
    }

    fun initDeviceInfo(application : AppCompatActivity){
        deviceInfo = DeviceInfo(application)
    }
    fun deviceIsOneSim(): Boolean? = (deviceInfo?.getTelSimCount() == 1)
    fun deviceSimOneNumber(): String? = deviceInfo?.getTelNumberSim1()

    fun getUserBalance(){
        requestResultHandler.post(Runnable {
            val savedToken = getSavedToken()
            if (savedToken != null){
                val request = Request.Builder()
                    .url("${URL_GETUSERBALANCE}token=$savedToken")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        val mMessage = Message()
                        mMessage.obj = ServerResponseData(
                            isSuccessful = false,
                            header = ServerResponseData.HEADER_ERROR,
                            data = "api don`t response"
                        )
                        requestResultHandler.handleMessage(mMessage)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = JSONObject(response.body()?.string())
                        val mMessage = Message()
                        if (json.getInt("success") == 1){
                            mMessage.obj = ServerResponseData(
                                isSuccessful = true,
                                header = ServerResponseData.HEADER_BALANCE,
                                data = json.getString("data")
                            )
                        }else{
                            mMessage.obj = ServerResponseData(
                                isSuccessful = false,
                                header = ServerResponseData.HEADER_ERROR,
                                data = json.getString("err")
                            )
                        }

                        requestResultHandler.handleMessage(mMessage)
                    }

                })

            }else{
                val mMessage = Message()
                mMessage.obj = ServerResponseData(
                    isSuccessful = false,
                    header = ServerResponseData.HEADER_ERROR,
                    data = "token in sharedPreferences is null"
                )
                requestResultHandler.handleMessage(mMessage)
            }



        })
    }

    fun login(tel :String, pass: String){
        requestResultHandler.post(Runnable {
            val request = Request.Builder()
                .url("${URL_LOGIN}user_name=${tel.removePrefix("+")}&user_pass=${pass}")
                .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val mMessage = Message()
                    mMessage.obj = ServerResponseData(
                        isSuccessful = false,
                        header = ServerResponseData.HEADER_ERROR,
                        data = "api don`t response"
                    )
                    requestResultHandler.handleMessage(mMessage)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()?.string())
                    val mMessage = Message()

                    if (json.getInt("success") == 1){
                        val token = json.getString("token")
                        if (token != null){
                            saveToken(token)
                            mMessage.obj = ServerResponseData(
                                isSuccessful = true,
                                header = ServerResponseData.HEADER_LOGIN,
                                data = null
                            )
                        }else{
                            mMessage.obj = ServerResponseData(
                                isSuccessful = false,
                                header = ServerResponseData.HEADER_ERROR,
                                data = "server token generation error"
                            )
                        }
                    }else{
                        mMessage.obj = ServerResponseData(
                            isSuccessful = false,
                            header = ServerResponseData.HEADER_ERROR,
                            data = json.getString("err")
                        )
                    }

                    requestResultHandler.handleMessage(mMessage)
                }

            })

        })

    }

    fun register(tel :String, pass: String){
        requestResultHandler.post(Runnable {
            val request = Request.Builder()
                .url("${URL_REGISTER}user_name=${tel.removePrefix("+")}&user_pass=${pass}")
                .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val mMessage = Message()
                    mMessage.obj = ServerResponseData(
                        isSuccessful = false,
                        header = ServerResponseData.HEADER_ERROR,
                        data = "api don`t response"
                    )
                    requestResultHandler.handleMessage(mMessage)
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()?.string())
                    val mMessage = Message()

                    if (json.getInt("success") == 1){
                        val token = json.getString("token")
                        if (token != null){
                            saveToken(token)
                            mMessage.obj = ServerResponseData(
                                isSuccessful = true,
                                header = ServerResponseData.HEADER_REGISTER,
                                data = null
                            )
                        }else{
                            mMessage.obj = ServerResponseData(
                                isSuccessful = false,
                                header = ServerResponseData.HEADER_ERROR,
                                data = "server token generation error"
                            )
                        }
                    }else{
                        mMessage.obj = ServerResponseData(
                            isSuccessful = false,
                            header = ServerResponseData.HEADER_ERROR,
                            data = json.getString("err")
                        )
                    }


                    requestResultHandler.handleMessage(mMessage)
                }

            })

        })

    }

    fun sendMoney(receiverTel: String, amount : Int){
        requestResultHandler.post(Runnable {
            val savedToken = getSavedToken()
            if (savedToken != null){
                val request = Request.Builder()
                    .url("${URL_SEND_MONEY}retriever_user_name=${receiverTel.removePrefix("+")}&amount=${amount}&user_sender_token=${savedToken}")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        val mMessage = Message()
                        mMessage.obj = ServerResponseData(
                            isSuccessful = false,
                            header = ServerResponseData.HEADER_ERROR,
                            data = "api don`t response"
                        )
                        requestResultHandler.handleMessage(mMessage)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = JSONObject(response.body()?.string())
                        val mMessage = Message()
                        if (json.getInt("success") == 1){
                            mMessage.obj = ServerResponseData(
                                isSuccessful = true,
                                header = ServerResponseData.HEADER_SEND_MONEY,
                                data = json.getString("data")
                            )
                        }else{
                            mMessage.obj = ServerResponseData(
                                isSuccessful = false,
                                header = ServerResponseData.HEADER_ERROR,
                                data = json.getString("err")
                            )
                        }
                        requestResultHandler.handleMessage(mMessage)
                    }

                })
            }else{
                val mMessage = Message()
                mMessage.obj = ServerResponseData(
                    isSuccessful = false,
                    header = ServerResponseData.HEADER_ERROR,
                    data = "token in sharedPreferences is null"
                )
                requestResultHandler.handleMessage(mMessage)
            }

        })
    }

    fun getItemsHistory(){
        requestResultHandler.post(Runnable {
            val savedToken = getSavedToken()
            if (savedToken != null){

                val request = Request.Builder()
                    .url("${URL_TRANSACTION_HISTORY}token=$savedToken")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        val mMessage = Message()
                        mMessage.obj = ServerResponseData(
                            isSuccessful = false,
                            header = ServerResponseData.HEADER_ERROR,
                            data = "api don`t response"
                        )
                        requestResultHandler.handleMessage(mMessage)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = JSONObject(response.body()?.string())
                        val mMessage = Message()
                        if (json.getInt("success") == 1){

                            val itemListJSON = json.getJSONArray("data")
                            val itemListCount = itemListJSON.length()

                            val rez = mutableListOf<TransactionRecycleData>()
                            for (i in 0 until itemListCount){
                                val row = (itemListJSON[i] as JSONObject)
                                rez.add(TransactionRecycleData(
                                    transactionFromText = row.getString("from_user_name"),
                                    transactionToText = row.getString("to_user_name"),
                                    transactionAmountText = row.getString("amount"),
                                    transactionTimeText = row.getString("at_time")
                                ))
                            }

                            mMessage.obj = ServerResponseData(
                                isSuccessful = true,
                                header = ServerResponseData.HEADER_TRANSACTION_HISTORY,
                                data = rez
                            )
                        }else{
                            mMessage.obj = ServerResponseData(
                                isSuccessful = false,
                                header = ServerResponseData.HEADER_ERROR,
                                data = json.getString("err")
                            )
                        }
                        requestResultHandler.handleMessage(mMessage)
                    }

                })

            }else{
                val mMessage = Message()
                mMessage.obj = ServerResponseData(
                    isSuccessful = false,
                    header = ServerResponseData.HEADER_ERROR,
                    data = "token in sharedPreferences is null"
                )
                requestResultHandler.handleMessage(mMessage)
            }

        })
    }

    private val requestResultHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            msg?.let {
                serverResponse.postValue(msg.obj as ServerResponseData)
                val delayCleaner = object : Handler(Looper.getMainLooper()){}
                delayCleaner.postDelayed(Runnable {
                    serverResponse.postValue(null)
                }, 50)
            }
        }
    }

}
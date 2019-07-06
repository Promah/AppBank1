package com.example.appbank1

data class ServerResponseData(val isSuccessful:Boolean, val header : String, val data : Any?) {
    companion object{
        val HEADER_BALANCE = "HEADER_BALANCE"
        val HEADER_ERROR = "HEADER_ERROR"
        val HEADER_LOGIN = "HEADER_LOGIN"
        val HEADER_REGISTER = "HEADER_REGISTER"
        val HEADER_SEND_MONEY = "HEADER_SEND_MONEY"
        val HEADER_TRANSACTION_HISTORY = "HEADER_TRANSACTION_HISTORY"
    }
}
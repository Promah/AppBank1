package com.example.appbank1.logic

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager


class DeviceInfo(application : AppCompatActivity) {

    private var telNumber: String? = null
    private var telSimCount : Int? = null

     init {
        if (ContextCompat.checkSelfPermission(application, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(application, android.Manifest.permission.READ_PHONE_STATE)) {
            } else { ActivityCompat.requestPermissions(application, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2) }
        }

        val tm = application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        @SuppressLint("HardwareIds")
        telNumber = tm.line1Number
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             telSimCount = tm.phoneCount
         }
     }

    fun getTelNumberSim1(): String? = telNumber

    fun getTelSimCount(): Int? = telSimCount


}
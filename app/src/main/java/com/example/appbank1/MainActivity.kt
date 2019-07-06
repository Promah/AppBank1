package com.example.appbank1

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var model : Communicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this).get(Communicator::class.java)
        model.initDeviceInfo(this)
        model.initSharedPreferences(this)

//        model.getServerResponse().observe(this, Observer {
//            it?.let {
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            }
//        })



//
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
//            } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 2) }
//        }

//        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//        val telMunber = tm.line1Number
//
//        textOne.text = telMunber

    }

}

package com.example.khiladi

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity


class Utils(var context: Context) {
    private val dialog = ProgressDialog(context)

    fun showLoading(){
        dialog.setTitle("Loading")
        dialog.show()
    }

    fun hideLoading(){
        dialog.setTitle("Loading")
        dialog.hide()
    }

     fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun dialogToTurnOnWifi(){
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("Wifi is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Goto Settings To Enable WIFI",
                DialogInterface.OnClickListener { dialog, id ->
                    val callGPSSettingIntent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                    startActivity(context,callGPSSettingIntent,null)
                })
        alertDialogBuilder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = alertDialogBuilder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Goto Settings Page To Enable GPS",
                DialogInterface.OnClickListener { dialog, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(context,callGPSSettingIntent,null)
                })
        alertDialogBuilder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert = alertDialogBuilder.create()
        alert.setCancelable(false)
        alert.show()
    }

    fun isGPSEnabled(): Boolean {
        val locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
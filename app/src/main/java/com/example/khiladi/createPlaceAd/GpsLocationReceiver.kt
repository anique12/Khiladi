package com.example.khiladi.createPlaceAd

import android.content.Intent
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.Context
import android.location.LocationManager


class GpsLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action!!.matches(LocationManager.PROVIDERS_CHANGED_ACTION.toRegex())) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(context,"Gps disable",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context,"Gps enabled",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
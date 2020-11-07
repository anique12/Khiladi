package com.example.khiladi


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.khiladi.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this,R.layout.activity_main)
        setupNavigation()

    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment)
        nav_view.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.chat || destination.id == R.id.feed || destination.id==R.id.ads || destination.id == R.id.more || destination.id == R.id.home) {
                nav_view.visibility = View.VISIBLE
            } else {
                nav_view.visibility = View.GONE
            }
        }
    }
}

package com.aaa.schooltracker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.aaa.schooltracker.R
import com.aaa.schooltracker.databinding.ActivityMainBinding

/**
 * The main activity of the application with the nav controller
 * and the bottom navigation
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)
    }
}
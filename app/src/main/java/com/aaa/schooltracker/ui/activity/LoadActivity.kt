/**
 * Name: LoadActivity.kt
 * Date: 5/11/2020
 * @author: Abdallah Alqashqish
 * Functionality: Controls the loading page
 */

package com.aaa.schooltracker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.aaa.schooltracker.R

class LoadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_page)

        //Delay
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this@LoadActivity, MainActivity::class.java))
            finish()
        }, 4000)
    }
}
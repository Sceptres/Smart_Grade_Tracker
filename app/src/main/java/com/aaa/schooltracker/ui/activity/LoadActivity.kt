package com.aaa.schooltracker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.aaa.schooltracker.R

/**
 * The loading screen activity
 *
 * @author Abdallah Alqashqish
 * @version v3.1
 */
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
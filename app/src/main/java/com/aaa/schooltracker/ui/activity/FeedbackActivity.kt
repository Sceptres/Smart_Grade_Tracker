package com.aaa.schooltracker.ui.activity

import android.app.Activity
import com.aaa.schooltracker.R
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_feedback.*

/**
 * Opens the feedback google form
 * @author Abdallah Alqashqish
 */
class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        feedbackWebView.settings.javaScriptEnabled = true
        //Open the form
        feedbackWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSfwTSAC_wA-hCz_OaNooyOO3jh5Gb0qR5DDWQULKLx1ojPEcQ/viewform?usp=sf_link")
    }


    //Function that controls the back arrow at the bottom of the screen
    override fun onBackPressed() {
    }

    /**
     * Opens a new activity
     * @param a The activity
     */
    private fun startReturnActivity(a: Activity){
        startActivity(Intent(this@FeedbackActivity, a::class.java))
        finish()
    }

}
package com.mangoapps.contactmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.mangoapps.contactmanager.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : Activity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // on below line we are calling handler to run a task
        // for specific time interval
        // on below line we are calling handler to run a task
        // for specific time interval
        Handler().postDelayed({ // on below line we are
            // creating a new intent
            val i = Intent(this, MainActivity::class.java)

            // on below line we are
            // starting a new activity.
            startActivity(i)

            // on the below line we are finishing
            // our current activity.
            finish()
        }, 500)
    }
}
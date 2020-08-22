package com.exoplayer.radio.service

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            ContextCompat.startForegroundService(this, Intent(this, RadioService::class.java))
        }

    }
}
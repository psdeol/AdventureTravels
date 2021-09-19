package com.example.adventuretravels

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class AboutActivity : AppCompatActivity() {
    companion object {
        var darkModeOn = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val buttonDark: Button = findViewById(R.id.button_dark_mode)

        buttonDark.setOnClickListener {
            if (darkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            darkModeOn = !darkModeOn

        }
    }
}
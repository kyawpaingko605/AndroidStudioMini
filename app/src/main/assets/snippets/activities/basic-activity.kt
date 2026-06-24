package com.example.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class BasicActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var button: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)
        
        // Set click listener
        button.setOnClickListener {
            textView.text = "Button clicked!"
        }
    }
    
    override fun onStart() {
        super.onStart()
        // Activity is about to become visible
    }
    
    override fun onResume() {
        super.onResume()
        // Activity is now visible
    }
    
    override fun onPause() {
        super.onPause()
        // Activity is about to be paused
    }
    
    override fun onStop() {
        super.onStop()
        // Activity is no longer visible
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Activity is about to be destroyed
    }
}

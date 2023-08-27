package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val string = intent.getStringExtra("displayText")
        val textView: TextView = findViewById(R.id.textView)
        textView.text = string
    }

    fun goBack(view: View?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
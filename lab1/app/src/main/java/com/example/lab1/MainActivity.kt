package com.example.lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnClickHandler(view: View?) {
        val btn = view as Button
        val txt = btn.text.toString()
        val intent = Intent(this, SecondActivity::class.java)
        val argsBundle = Bundle()
        argsBundle.putString("displayText", txt)
        intent.putExtras(argsBundle)
        startActivity(intent)
    }
}


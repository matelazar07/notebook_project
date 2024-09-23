package com.example.konyvguilde

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AppHelper :  AppCompatActivity() {
    private lateinit var btnBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        btnBack = findViewById(R.id.btnBack)
       btnBack.setOnClickListener { gotoMain() }
    }
    private fun gotoMain()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

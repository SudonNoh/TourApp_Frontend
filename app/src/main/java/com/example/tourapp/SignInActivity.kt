package com.example.tourapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<TextView>(R.id.SignInPageSignUpBtn).apply {
            this.setOnClickListener {
                this.setTextColor(Color.RED)
                startActivity(
                    Intent(this@SignInActivity, SignUpActivity::class.java)
                )
            }
        }
    }
}
package com.example.tourapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUpActivity : AppCompatActivity() {

    private var username: String = ""
    private var mobile: String = ""
    private var password1: String = ""
    private var password2: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<EditText>(R.id.signUpPageIDEdit).doAfterTextChanged {
            username = it.toString()
        }
        findViewById<EditText>(R.id.signUpPageMobileEdit).doAfterTextChanged {
            mobile = it.toString()
        }
        findViewById<EditText>(R.id.signupPagePWEdit1).doAfterTextChanged {
            password1 = it.toString()
        }
        findViewById<EditText>(R.id.signupPagePWEdit2).doAfterTextChanged {
            password2 = it.toString()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://2d8f-210-217-121-40.jp.ngrok.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitService = retrofit.create(RetrofitService::class.java)

        findViewById<TextView>(R.id.signUpPageBtn).setOnClickListener {
            it.setBackgroundColor(Color.BLACK)
            if (password1 != password2) {
                Toast.makeText(
                    this@SignUpActivity,
                    "비밀번호가 일치하지 않습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password1.length < 8) {
                Toast.makeText(
                    this@SignUpActivity,
                    "비밀번호의 길이가 짧습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val user = HashMap<String, Any>()
                user["username"] = username
                user["mobile"] = mobile
                user["password"] = password1

                retrofitService.signUp(user).enqueue(object : Callback<UserInfo> {
                    override fun onResponse(call: Call<UserInfo>, response: Response<UserInfo>) {
                        if (response.isSuccessful) {
                            val userInfo: UserInfo? = response.body()
                            val sharedPreferences =
                                getSharedPreferences("user_info", Context.MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = sharedPreferences.edit()

                            if (userInfo != null) {
                                editor.putString("access_token", userInfo.user.token.access_token)
                                editor.putString("refresh_token", userInfo.user.token.refresh_token)
                                editor.putString("user_id", userInfo.user.id.toString())
                                editor.apply()

                                startActivity(
                                    Intent(
                                        this@SignUpActivity,
                                        SignInActivity::class.java
                                    )
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                    }
                })
            }
        }
    }
}
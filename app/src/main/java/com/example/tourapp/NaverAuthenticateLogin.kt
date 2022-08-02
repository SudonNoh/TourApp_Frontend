package com.example.tourapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tourapp.databinding.ActivityNaverAuthenticateLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthBehavior
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse


class NaverAuthenticateLogin : AppCompatActivity() {

    lateinit var binding: ActivityNaverAuthenticateLoginBinding
    lateinit var context: Context

    lateinit var naverClientId: String
    lateinit var naverClientSecret: String
    lateinit var naverAppName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNaverAuthenticateLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        context = this
        naverClientId = getString(R.string.naver_client_id)
        naverClientSecret = getString(R.string.naver_client_secret)
        naverAppName = getString(R.string.app_name)

        NaverIdLoginSDK.apply {
            showDevelopersLog(true)
            initialize(context, naverClientId, naverClientSecret, naverAppName)
        }

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(result: NidProfileResponse) {
                val email = result.profile?.email
                Toast.makeText(this@NaverAuthenticateLogin, "$email 로 로그인 성공!", Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val profileErrorCode = NaverIdLoginSDK.getLastErrorCode().code
                val profileErrorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    context,
                    "errorCode:$profileErrorCode, errorDesc:$profileErrorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.login.setOnClickListener {
            NaverIdLoginSDK.behavior = NidOAuthBehavior.DEFAULT
            NaverIdLoginSDK.authenticate(context, object : OAuthLoginCallback {
                override fun onSuccess() {
                    updateView()
                    NidOAuthLogin().callProfileApi(profileCallback)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast.makeText(
                        context,
                        "errorCode:$errorCode, errorDesc:$errorDescription",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(errorCode: Int, message: String) {
                    onFailure(errorCode, message)
                }
            })
        }

        binding.logout.setOnClickListener {
            NaverIdLoginSDK.logout()
            val logoutState:String = NaverIdLoginSDK.getState().toString()
            Toast.makeText(
                context,
                "로그아웃 성공 $logoutState",
                Toast.LENGTH_SHORT
            ).show()

            updateView()
        }
    }

    fun updateView() {
        binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
        binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
        binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
        binding.tvType.text = NaverIdLoginSDK.getTokenType()
        binding.tvState.text = NaverIdLoginSDK.getState().toString()
    }
}
package com.example.tourapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tourapp.databinding.ActivityNaverButtonLoginBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NaverButtonLogin : AppCompatActivity() {

    lateinit var binding: ActivityNaverButtonLoginBinding
    lateinit var context: Context

    lateinit var naverClientId: String
    lateinit var naverClientSecret: String
    lateinit var naverAppName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNaverButtonLoginBinding.inflate(layoutInflater)
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

        binding.buttonOAuthLoginImg.setOAuthLoginCallback(object : OAuthLoginCallback {
            override fun onSuccess() {
                getId()
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

    private fun getId() {
        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(result: NidProfileResponse) {
                Toast.makeText(
                    context,
                    "$result",
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvApiResult.text = result.toString()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    context,
                    "errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvApiResult.text = ""
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}
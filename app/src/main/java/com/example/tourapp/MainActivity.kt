package com.example.tourapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tourapp.databinding.ActivityMainBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var context: Context

    lateinit var naverClientId: String
    lateinit var naverClientSecret: String
    lateinit var naverAppName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
                get_id()
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

    private fun get_id() {
        NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                Toast.makeText(
                    context,
                    "$response",
                    Toast.LENGTH_SHORT
                ).show()
                binding.tvApiResult.text = response.toString()
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
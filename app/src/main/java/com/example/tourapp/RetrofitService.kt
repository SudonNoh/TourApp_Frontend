package com.example.tourapp

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class UserInfo(
    val user: User
)

class User(
    val id: Int,
    val username: String,
    val email: String,
    val token: Token,
    val is_active: Boolean
)

class Token(
    val access_token: String,
    val refresh_token: String
)


interface RetrofitService {
    @POST("user/signup")
    @FormUrlEncoded
    fun signUp(
        @FieldMap params: HashMap<String, Any>
    ): Call<UserInfo>

}
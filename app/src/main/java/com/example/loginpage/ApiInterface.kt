package com.example.loginpage

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("createUser")
    fun addUser(@Body userDat: userDataItem): Call<Boolean>

//    @GET("products/backend")
//    fun checkUser(@Query("s") s: String) : Call<Boolean>

    @POST("/userlist")
    fun data(@Body username1: username) :Call<List<String>>

    @POST("/login")
    fun userExists(@Body userdata : RegisterData) : Call<Boolean>

    @POST("/insertPublicKey")
    fun insertKey(@Body publicKeyCredential: PublicKeyCredential) : Call<Boolean>

    @POST("/getPublicKey")
    fun getPublickey(@Body user : username) : Call<List<String>>
//    fun userExists(@Query("username") username: String,@Query("password") password: String) : Call<Boolean>
}
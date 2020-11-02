package com.naeem.fetchgithubuserslist_mvvm_room.data.remote.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.naeem.fetchgithubuserslist_mvvm_room.model.UserDetailModel_Api_Response
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RetofitService {

    @GET("users")
    suspend fun getUsers(
        @Query("since") query: Int
    ): Response<JsonArray>


    @GET("users/{userid}")
    suspend fun getuserdetail(
        @Path("userid") userid: String
    ): Response<UserDetailModel_Api_Response>




    
    
    companion object {
        const val FOODIUM_API_URL = "https://api.github.com/"
    }
}

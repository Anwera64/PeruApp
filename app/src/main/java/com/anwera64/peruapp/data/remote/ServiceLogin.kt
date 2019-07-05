package com.anwera64.peruapp.data.remote

import com.google.gson.JsonElement
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST

interface ServiceLogin {

    @POST("url")
    fun login(@Header("Authorization") auth: String, @Field("email") email: String, @Field("password") password: String): Observable<JsonElement>
}
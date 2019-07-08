package com.anwera64.peruapp.data.remote

import com.anwera64.peruapp.data.model.Token
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ServiceLogin {

    @POST("oauth/token")
    @FormUrlEncoded
    fun login(
        @Header("Authorization") auth: String,
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String
    ): Observable<Response<Token>>
}
package com.anwera64.peruapp.data.remote

import com.anwera64.peruapp.data.model.Task
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface ServiceTask {

    @GET("/rest/v1/secured/item")
    fun getTasks(@Header("Authorization") auth: String): Observable<Response<List<Task>>>

    @POST("/rest/v1/secured/item")
    fun createTask(@Header("Authorization") auth: String, @Body body: JSONObject): Observable<Response<Task>>

    @DELETE("/rest/v1/secured/item/{id}")
    fun deleteTask(@Header("Authorization") auth: String, @Path("id") id: String): Observable<Response<Void>>
}
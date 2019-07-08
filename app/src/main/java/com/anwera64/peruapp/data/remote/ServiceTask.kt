package com.anwera64.peruapp.data.remote

import com.anwera64.peruapp.data.model.Pageable
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.data.model.TaskRequest
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ServiceTask {

    @GET("/rest/v1/secured/item")
    fun getTasks(@Header("Authorization") auth: String, @Query("page") page: Int): Observable<Response<Pageable<Task>>>

    @POST("/rest/v1/secured/item")
    fun createTask(@Header("Authorization") auth: String, @Body body: TaskRequest): Observable<Response<Task>>

    @DELETE("/rest/v1/secured/item/{id}")
    fun deleteTask(@Header("Authorization") auth: String, @Path("id") id: String): Observable<Response<Void>>
}
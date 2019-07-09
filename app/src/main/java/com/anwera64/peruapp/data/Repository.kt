package com.anwera64.peruapp.data

import com.anwera64.peruapp.BuildConfig
import com.anwera64.peruapp.data.model.Pageable
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.data.model.TaskRequest
import com.anwera64.peruapp.data.model.Token
import com.anwera64.peruapp.data.remote.ServiceLogin
import com.anwera64.peruapp.data.remote.ServiceTask
import io.reactivex.Observable
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository private constructor() {

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        private val certPinning = CertificatePinner.Builder()
            .add("api.notbank.pe", BuildConfig.SSL)
            .build()

        private val httpClient = OkHttpClient.Builder()
            .certificatePinner(certPinning)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        private val serviceLogin = retrofit.create(ServiceLogin::class.java)
        private val serviceTask = retrofit.create(ServiceTask::class.java)

        fun getInstance(): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository()
                INSTANCE = instance
                return instance
            }
        }
    }

    fun login(email: String, password: String): Observable<Response<Token>> {
        return serviceLogin.login(BuildConfig.Auth, email, password, "password")
    }

    fun tasks(token: String, page: Int): Observable<Response<Pageable<Task>>> {
        return serviceTask.getTasks("Bearer $token", page)
    }

    fun deleteTask(id: String, token: String): Observable<Response<Void>> {
        return serviceTask.deleteTask("Bearer $token", id)
    }

    fun createTask(title: String, description: String, token: String): Observable<Response<Task>> {
        val body = TaskRequest(title, description)
        return serviceTask.createTask("Bearer $token", body)
    }
}
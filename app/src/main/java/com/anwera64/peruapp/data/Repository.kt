package com.anwera64.peruapp.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.MediatorLiveData
import com.anwera64.peruapp.BuildConfig
import com.anwera64.peruapp.data.local.AppDatabase
import com.anwera64.peruapp.data.model.Task
import com.anwera64.peruapp.data.model.Token
import com.anwera64.peruapp.data.remote.ServiceLogin
import com.google.gson.JsonElement
import io.reactivex.Observable
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Repository private constructor(private val database: AppDatabase) {

    var mTasks: MediatorLiveData<List<Task>> = MediatorLiveData()

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        private val certPinning = CertificatePinner.Builder()
            .add("notbank.com", "sha256/vn25ZeRRoCa7WBlcPDaJfYhMYgDHxjrXmHBK1VJiHps=")
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

        fun getInstance(database: AppDatabase): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository(database)
                INSTANCE = instance
                return instance
            }
        }
    }

    init {
        mTasks.addSource(database.taskDAO().getAll()) { taskEntities -> postTasks(taskEntities) }
    }

    fun login(email: String, password: String): Observable<Response<Token>> {
        return serviceLogin.login(BuildConfig.Auth, email, password, "password")
    }

    private fun postTasks(taskEntities: List<Task>) {
        if (database.mIsDatabaseCreated.value != null) {
            mTasks.postValue(taskEntities)
        }
    }

    @WorkerThread
    suspend fun insertTask(task: Task) {
        database.taskDAO().insertTask(task)
    }

    @WorkerThread
    suspend fun deleteTask(task: Task) {
        database.taskDAO().deleteTask(task)
    }

    fun getTask(id: String): Task? {
        return database.taskDAO().getTask(id)
    }
}
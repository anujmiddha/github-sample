package com.moldedbits.githubsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.model.RepositoriesResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        testApi()
    }

    fun testApi() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val service = retrofit.create<GitHubService>(GitHubService::class.java)
        service.trendingRepos("android").enqueue(object : Callback<RepositoriesResponse> {
            override fun onFailure(call: Call<RepositoriesResponse>, t: Throwable) {
                Timber.e(t, "Failure")
            }

            override fun onResponse(call: Call<RepositoriesResponse>, response: Response<RepositoriesResponse>) {
                Timber.d("Success! Found %s repos", response.body()?.totalCount)
            }

        })
    }
}

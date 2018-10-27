package com.moldedbits.githubsample.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.util.ApplicationSchedulerProvider
import com.moldedbits.githubsample.util.SchedulerProvider
import com.moldedbits.githubsample.view.detail.DetailViewModel
import com.moldedbits.githubsample.view.list.ListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single<GitHubService> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'hh:mm:ssZ")
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        retrofit.create<GitHubService>(GitHubService::class.java)
    }

    viewModel { ListViewModel(get(), get()) }

    viewModel { DetailViewModel(get()) }
}

val rxModule = module {

    single { ApplicationSchedulerProvider() as SchedulerProvider }
}

val githubApp = listOf(appModule, rxModule)
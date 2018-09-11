package com.moldedbits.githubsample.api

import com.moldedbits.githubsample.model.RepositoriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    @GET("/search/repositories?sort=stars&order=desc")
    fun trendingRepos(@Query("q") query: String): Call<RepositoriesResponse>
}
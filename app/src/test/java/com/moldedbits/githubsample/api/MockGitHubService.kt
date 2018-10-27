package com.moldedbits.githubsample.api

import com.google.gson.Gson
import com.moldedbits.githubsample.model.RepositoriesResponse
import com.moldedbits.githubsample.model.Repository
import io.reactivex.Single

class MockGitHubService(private val gson: Gson,
                        private val jsonReader: JsonReader) : GitHubService {

    override fun trendingRepos(query: String, page: Int,
                               perPage: Int): Single<RepositoriesResponse> {

        return Single.create { s ->
            val repositories = gson.fromJson(jsonReader.readJsonFile(LIST_FILE),
                    RepositoriesResponse::class.java)
            s.onSuccess(repositories)
        }
    }

    override fun repoDetails(owner: String, repo: String): Single<Repository> {

        return Single.create { s ->
            val repository = gson.fromJson(jsonReader.readJsonFile(DETAILS_FILE),
                    Repository::class.java)
            s.onSuccess(repository)
        }
    }

    companion object {
        private const val LIST_FILE = "repositories.json"
        private const val DETAILS_FILE = "repository.json"
    }
}
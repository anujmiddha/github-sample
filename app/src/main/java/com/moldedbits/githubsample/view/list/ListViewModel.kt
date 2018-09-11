package com.moldedbits.githubsample.view.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.model.RepositoriesResponse
import com.moldedbits.githubsample.model.Repository
import com.moldedbits.githubsample.util.RxViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ListViewModel(private val gitHubService: GitHubService) : RxViewModel() {

    private val mRepos = MutableLiveData<List<Repository>>()
    val repos: LiveData<List<Repository>>
        get() = mRepos

    fun fetchRepos() {
        launch {
            gitHubService.trendingRepos("android")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onReposFetched, this::onError)
        }
    }

    private fun onReposFetched(response: RepositoriesResponse) {
        mRepos.value = response.items
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable, "could not fetch repos")
    }
}
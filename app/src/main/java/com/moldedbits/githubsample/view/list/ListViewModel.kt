package com.moldedbits.githubsample.view.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.model.RepositoriesResponse
import com.moldedbits.githubsample.util.RxViewModel
import com.moldedbits.githubsample.view.ErrorState
import com.moldedbits.githubsample.view.LoadingState
import com.moldedbits.githubsample.view.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListViewModel(private val gitHubService: GitHubService) : RxViewModel() {

    private val mStates = MutableLiveData<State>()
    val states: LiveData<State>
        get() = mStates

    /**
     * Fetch the trending repositories
     *
     * @param currentCount Count of currently loaded repos
     */
    fun fetchRepos(currentCount: Int = 0) {
        mStates.value = LoadingState

        val page = currentCount / PAGE_SIZE + 1

        launch {
            gitHubService.trendingRepos(page = page, perPage = PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onReposFetched, this::onError)
        }
    }

    private fun onReposFetched(response: RepositoriesResponse) {
        mStates.value = RepoListState(response)
    }

    private fun onError(throwable: Throwable) {
        mStates.value = ErrorState(throwable)
    }

    data class RepoListState(val repositoriesResponse: RepositoriesResponse) : State()

    companion object {
        private const val PAGE_SIZE: Int = 30
    }
}
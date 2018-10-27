package com.moldedbits.githubsample.view.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.model.Repository
import com.moldedbits.githubsample.util.RxViewModel
import com.moldedbits.githubsample.util.SchedulerProvider
import com.moldedbits.githubsample.util.ext.with
import com.moldedbits.githubsample.view.ErrorState
import com.moldedbits.githubsample.view.State

class DetailViewModel(private val gitHubService: GitHubService,
                      private val scheduler: SchedulerProvider) : RxViewModel() {

    private val mStates = MutableLiveData<State>()
    val states: LiveData<State>
        get() = mStates

    fun fetchDetails(repository: Repository) {
        // Show the details we already have
        mStates.value = RepoDetailsState(repository, false)

        launch {
            gitHubService.repoDetails(owner = repository.owner.login, repo = repository.name)
                    .with(scheduler)
                    .subscribe(this::onRepoFetched, this::onError)
        }
    }

    private fun onRepoFetched(repository: Repository) {
        mStates.value = RepoDetailsState(repository, true)
    }

    private fun onError(throwable: Throwable) {
        mStates.value = ErrorState(throwable)
    }

    data class RepoDetailsState(val repository: Repository, val remote: Boolean) : State()
}
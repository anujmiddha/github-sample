package com.moldedbits.githubsample.view.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.view.ErrorState
import com.moldedbits.githubsample.view.LoadingState
import com.moldedbits.githubsample.view.State
import kotlinx.android.synthetic.main.activity_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ListActivity : AppCompatActivity() {

    private val viewModel: ListViewModel by viewModel()

    private var isUpdating: Boolean = false
    private var isFinished: Boolean = false

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private val adapter: RepositoriesAdapter by lazy { RepositoriesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initViews()

        viewModel.states.observe(this, stateObserver)
        fetchRepos(null)
    }

    private fun initViews() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        RxRecyclerView
                .scrollEvents(recyclerView)
                .filter(this::shouldUpdate)
                .filter(this::hasScrolledToLast)
                .subscribe(this::fetchRepos)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun shouldUpdate(event: RecyclerViewScrollEvent?): Boolean {
        return !isUpdating && !isFinished
    }

    @Suppress("UNUSED_PARAMETER")
    private fun hasScrolledToLast(event: RecyclerViewScrollEvent?): Boolean {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        return (visibleItemCount + pastVisibleItems) >= totalItemCount
    }

    @Suppress("UNUSED_PARAMETER")
    private fun fetchRepos(event: RecyclerViewScrollEvent?) {
        viewModel.fetchRepos(adapter.itemCount)
    }

    private val stateObserver = Observer<State> {
        when (it) {
            is LoadingState -> {
                isUpdating = true
                isFinished = false

                if (adapter.itemCount == 0) {
                    emptyView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    recyclerView.post { adapter.showLoading() }
                }
            }
            is ListViewModel.RepoListState -> {
                isUpdating = false
                isFinished = it.repositoriesResponse.totalCount == adapter.itemCount

                emptyView.visibility = View.GONE
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                recyclerView.post { adapter.hideLoading() }

                val previousCount = adapter.itemCount
                adapter.addAll(it.repositoriesResponse.items)
                adapter.notifyItemRangeInserted(previousCount, it.repositoriesResponse.items.size)
            }
            is ErrorState -> {
                isUpdating = false
                isFinished = true

                if (adapter.itemCount == 0) {
                    emptyView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE

                    emptyView.text = getString(R.string.error_message)
                } else {
                    emptyView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                Timber.e(it.error, "Could not fetch repos")

                recyclerView.post { adapter.hideLoading() }
            }
        }
    }
}

package com.moldedbits.githubsample.view.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.view.ErrorState
import com.moldedbits.githubsample.view.LoadingState
import com.moldedbits.githubsample.view.State
import kotlinx.android.synthetic.main.activity_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ListActivity : AppCompatActivity() {

    private val viewModel: ListViewModel by viewModel()

    private val adapter: RepositoriesAdapter by lazy { RepositoriesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initViews()

        viewModel.states.observe(this, Observer<State> {
            when (it) {
                is LoadingState -> {
                    emptyView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                is ListViewModel.RepoListState -> {
                    emptyView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    Timber.d("Found ${it.repositoriesResponse.items.size} repositories")
                    adapter.addAll(it.repositoriesResponse.items)
                    adapter.notifyDataSetChanged()
                }
                is ErrorState -> {
                    emptyView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE

                    emptyView.text = getString(R.string.error_message)
                    Timber.e(it.error, "Could not fetch repos")
                }
            }
        })

        viewModel.fetchRepos()
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)

        recyclerView.adapter = adapter
    }
}

package com.moldedbits.githubsample.view.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.model.Repository
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class ListActivity : AppCompatActivity() {

    private val viewModel: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        viewModel.repos.observe(this, Observer<List<Repository>> {
            t -> Timber.d("Found ${t?.size} repositories")
        })

        viewModel.fetchRepos()
    }
}

package com.moldedbits.githubsample.view.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.util.GlideApp
import com.moldedbits.githubsample.view.ErrorState
import com.moldedbits.githubsample.view.State
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.android.viewmodel.ext.android.viewModel
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*


class RepositoryDetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModel()

    private val dateFormat: SimpleDateFormat by lazy {
        SimpleDateFormat("EEE, MMM d, yyyy" , Locale.getDefault())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        viewModel.states.observe(this, observer)
        viewModel.fetchDetails(intent.getParcelableExtra(KEY_REPO))
    }

    private val observer: Observer<State> = Observer {
        when (it) {
            is DetailViewModel.RepoDetailsState -> {
                // Populate repository
                title = it.repository.name

                fullNameView.text = it.repository.fullName
                ownerView.text = it.repository.owner.login
                descriptionView.text = it.repository.description
                it.repository.pushedAt?.let { date ->
                    lastUpdatedView.text = dateFormat.format(date)
                }

                watchersView.text = it.repository.watchersCount.toString()
                stargazersView.text = it.repository.stargazersCount.toString()

                val options = RequestOptions()
                options.circleCrop()

                GlideApp.with(this)
                        .load(it.repository.owner.avatarUrl)
                        .apply(options)
                        .placeholder(R.drawable.placeholder)
                        .fitCenter()
                        .into(avatar)

                if (it.remote) {
                    progressBar.visibility = View.GONE
                }

                if (it.repository.organization != null) {
                    organizationContainer.visibility = View.VISIBLE
                    organizationNameView.text = it.repository.organization.login

                    GlideApp.with(this)
                            .load(it.repository.organization.avatarUrl)
                            .apply(options)
                            .placeholder(R.drawable.placeholder)
                            .fitCenter()
                            .into(organizationAvatarView)
                }

                if (it.repository.license != null) {
                    licenseContainer.visibility = View.VISIBLE
                    licenseNameView.text = it.repository.license.name
                }
            }
            is ErrorState -> {
                progressBar.visibility = View.GONE
                Toast.makeText(this, R.string.details_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val KEY_REPO = "repo"
    }
}
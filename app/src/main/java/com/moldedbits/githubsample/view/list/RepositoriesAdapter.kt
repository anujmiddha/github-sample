package com.moldedbits.githubsample.view.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.model.Repository

class RepositoriesAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val items: MutableList<Repository> = mutableListOf()

    private var showLoading: Boolean = false

    fun addAll(repos: List<Repository>) {
        items.addAll(repos)
    }

    fun clear() {
        items.clear()
    }

    fun showLoading() {
        showLoading = true
        notifyItemInserted(items.size)
    }

    fun hideLoading() {
        showLoading = false
        notifyItemRemoved(items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_REPO -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_repository, parent, false)
                RepositoryViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size +
                (if (showLoading) 1 else 0) // Add 1 if we are showing loading indicator
    }

    override fun getItemViewType(position: Int): Int {
        if (showLoading && position == items.size) {
            return VIEW_TYPE_LOADING
        }

        return VIEW_TYPE_REPO
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_REPO -> (holder as RepositoryViewHolder).bind(items[position])
        }
    }

    companion object {
        private const val VIEW_TYPE_REPO = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class RepositoryViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val nameView: TextView = itemView.findViewById(R.id.nameView)
    private val descriptionView: TextView = itemView.findViewById(R.id.descriptionView)
    private val watchersView: TextView = itemView.findViewById(R.id.watchersView)
    private val stargazersView: TextView = itemView.findViewById(R.id.stargazersView)

    fun bind(repository: Repository) {
        nameView.text = repository.name
        descriptionView.text = repository.description
        watchersView.text = repository.watchersCount.toString()
        stargazersView.text = repository.stargazersCount.toString()
    }
}

class LoadingViewHolder(itemView: View) : BaseViewHolder(itemView)
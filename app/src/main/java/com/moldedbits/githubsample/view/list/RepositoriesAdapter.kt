package com.moldedbits.githubsample.view.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moldedbits.githubsample.R
import com.moldedbits.githubsample.model.Repository

class RepositoriesAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {

    private val items: MutableList<Repository> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addAll(repos: List<Repository>) {
        items.addAll(repos)
    }

    fun clear() {
        items.clear()
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val nameView: TextView = itemView.findViewById(R.id.nameView)

    fun bind(repository: Repository) {
        nameView.text = repository.name
    }
}
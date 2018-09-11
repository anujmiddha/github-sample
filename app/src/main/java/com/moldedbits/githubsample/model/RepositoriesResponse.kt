package com.moldedbits.githubsample.model

data class RepositoriesResponse(val totalCount: Int,
                                val incompleteResults: Boolean,
                                val items: List<Repository>)
package com.moldedbits.githubsample.model

data class RepositoriesResponse(val totalCount: Int,
                                val imcompleteResults: Boolean,
                                val items: List<Repository>)
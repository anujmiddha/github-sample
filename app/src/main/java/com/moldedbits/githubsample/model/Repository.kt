package com.moldedbits.githubsample.model

data class Repository(val id: Int,
                      val name: String,
                      val fullName: String,
                      val url: String,
                      val description: String,
                      val private: Boolean,
                      val stargazersCount: Int,
                      val watchersCount: Int,
                      val language: String,
                      val owner: User)

data class User(val login: String,
                val avatarUrl: String,
                val url: String)
package com.moldedbits.githubsample.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Repository(val id: Int,
                      val name: String,
                      val fullName: String,
                      val url: String,
                      val description: String?,
                      val private: Boolean,
                      val stargazersCount: Int,
                      val watchersCount: Int,
                      val language: String?,
                      val pushedAt: Date?,
                      val owner: User,
                      val organization: Organization?,
                      val license: License?) : Parcelable

@Parcelize
data class User(val login: String,
                val avatarUrl: String?,
                val url: String?) : Parcelable

@Parcelize
data class Organization(val login: String,
                        val avatarUrl: String?,
                        val url: String?) : Parcelable

@Parcelize
data class License(val key: String,
                   val name: String,
                   val url: String?) : Parcelable
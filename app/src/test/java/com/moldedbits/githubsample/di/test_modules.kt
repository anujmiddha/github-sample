package com.moldedbits.githubsample.di

import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.api.JsonReader
import com.moldedbits.githubsample.api.MockGitHubService
import com.moldedbits.githubsample.util.SchedulerProvider
import com.moldedbits.githubsample.util.TestSchedulerProvider
import org.koin.dsl.module.module

val testAppModule = module {

    single(override = true) { MockGitHubService(get(), get()) as GitHubService }

    single { JsonReader() }
}

val testRxModule = module {
    // provided components
    single(override = true) { TestSchedulerProvider() as SchedulerProvider }
}

val testApp = githubApp + testAppModule + testRxModule


package com.moldedbits.githubsample.di

import com.moldedbits.githubsample.util.SchedulerProvider
import com.moldedbits.githubsample.util.TestSchedulerProvider
import org.koin.dsl.module.module

val testRxModule = module {
    // provided components
    single(override = true) { TestSchedulerProvider() as SchedulerProvider }
}

val testApp = githubApp + testRxModule


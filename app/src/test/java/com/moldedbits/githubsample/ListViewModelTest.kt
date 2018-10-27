package com.moldedbits.githubsample

import android.arch.lifecycle.Observer
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.di.appModule
import com.moldedbits.githubsample.di.testApp
import com.moldedbits.githubsample.view.State
import com.moldedbits.githubsample.view.list.ListViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule
import org.junit.Rule

class ListViewModelTest : KoinTest {

    val viewModel: ListViewModel by inject()
    val service by inject<GitHubService>()

    @Mock
    lateinit var uiData: Observer<State>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin(testApp)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testGotList() {
        // Setup data
        val list = service.trendingRepos().blockingGet()

        // Observe
        viewModel.states.observeForever(uiData)

        viewModel.fetchRepos()

        // Setup state to notify
        val state = ListViewModel.RepoListState(list)

        // Has received data
        Assert.assertNotNull(viewModel.states.value)

        // Has been notified
        Mockito.verify(uiData).onChanged(state)
    }
}
package com.moldedbits.githubsample.view

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.moldedbits.githubsample.api.GitHubService
import com.moldedbits.githubsample.di.testApp
import com.moldedbits.githubsample.view.detail.DetailViewModel
import org.junit.*
import org.junit.rules.TestRule
import org.koin.standalone.StandAloneContext
import org.koin.standalone.inject
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DetailViewModelTest : KoinTest {

    private val viewModel: DetailViewModel by inject()
    private val service by inject<GitHubService>()

    @Mock
    lateinit var uiData: Observer<State>

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        StandAloneContext.startKoin(testApp)
    }

    @After
    fun after() {
        StandAloneContext.stopKoin()
    }

    @Test
    fun testGotDetails() {
        // Setup data
        val details = service.repoDetails("", "").blockingGet()

        // Observe
        viewModel.states.observeForever(uiData)

        viewModel.fetchDetails(details)

        // Setup state to notify
        val state = DetailViewModel.RepoDetailsState(details, false)

        // Has received data
        Assert.assertNotNull(viewModel.states.value)

        // Has been notified
        val order = Mockito.inOrder(uiData)
        order.verify(uiData).onChanged(argThat<DetailViewModel.RepoDetailsState> { it ->
            !it.remote
        })
        order.verify(uiData).onChanged(argThat<DetailViewModel.RepoDetailsState> { it ->
            it.remote
        })
        order.verifyNoMoreInteractions()
    }
}
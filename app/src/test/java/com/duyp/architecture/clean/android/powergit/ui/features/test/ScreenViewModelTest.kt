package com.duyp.architecture.clean.android.powergit.ui.features.test

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.duyp.architecture.clean.android.powergit.rules.TrampolineSchedulerRule
import io.reactivex.Completable
import io.reactivex.Observable
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

class ScreenViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testSchedulerRule = TrampolineSchedulerRule()

    private val usecase: TestUseCase = Mockito.mock(TestUseCase::class.java)

    private val viewModel = ScreenViewModel(usecase)

    private lateinit var stateObserver: LiveDataTestObserver<ScreenState>

    @Before
    fun setup() {
        stateObserver = LiveDataTestObserver()
        viewModel.liveData.observeForever(stateObserver)
    }

    @After
    fun teardown() {
        viewModel.liveData.removeObserver(stateObserver)
    }

    @Test
    fun `click button - success - should show correct message`() {
        Mockito.`when`(usecase.execute()).thenReturn(Completable.complete())
        viewModel.clickButton()

        stateObserver
                .assertValueAt(0) {
                    isLoading && !isButtonEnabled
                }
                .assertValueAt(1) {
                    !isLoading && isButtonEnabled && message == "success"
                }
    }
}

package com.duyp.architecture.clean.android.powergit.domain.usecases

import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class UseCaseTest<U : Any> {

    protected lateinit var mUsecase : U

    @Before
    fun setup() {
        mUsecase = createUseCase()
    }

    abstract fun createUseCase() : U
}
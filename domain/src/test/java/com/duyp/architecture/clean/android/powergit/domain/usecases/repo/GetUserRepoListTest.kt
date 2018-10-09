package com.duyp.architecture.clean.android.powergit.domain.usecases.repo

import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.repositories.RepoRepository
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUser
import com.duyp.architecture.clean.android.powergit.domain.usecases.UseCaseTest
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class GetUserRepoListTest: UseCaseTest<GetUserRepoList>(){

    @Mock private lateinit var mGetUser: GetUser

    @Mock private lateinit var mRepoRepository: RepoRepository

    override fun createUseCase(): GetUserRepoList {
        return GetUserRepoList(mRepoRepository, mGetUser)
    }

    @Test
    fun getRepoList_error() {
        whenever(mRepoRepository.getUserRepoList(any(), any(), any()))
                .thenReturn(Single.error(Exception("error")))

        mUsecase.getRepoList(ListEntity(), "abcd", FilterOptions())
                .test()
                .assertErrorMessage("error")

        verify(mRepoRepository).getUserRepoList(eq("abcd"), any(), eq(ListEntity.STARTING_PAGE))
    }

    @Test
    fun getRepoList_success() {
        whenever(mRepoRepository.getUserRepoList(any(), any(), any()))
                .thenReturn(Single.just(ListEntity()))

        mUsecase.getRepoList(ListEntity(next = 5), "duyp", FilterOptions())
                .test()
                .assertNoErrors()
                .assertValueCount(1)

        verify(mRepoRepository).getUserRepoList(eq("duyp"), any(), eq(5))
    }

    @Test
    fun getRepoList_mergeWithPrevious() {
        whenever(mRepoRepository.getUserRepoList(any(), any(), any()))
                .thenReturn(Single.just(ListEntity(next = 4, last = 5, items = listOf(
                        RepoEntity(id = 2),
                        RepoEntity(id = 3)
                ))))

        mUsecase.getRepoList(ListEntity(next = 3, last = 5, items = listOf(
                RepoEntity(id = 0),
                RepoEntity(id = 1)
        )), "duyp", FilterOptions())
                .test()
                .assertNoErrors()
                .assertValue { it.getNextPage() == 4 && it.items.size == 4 }

        verify(mRepoRepository).getUserRepoList(eq("duyp"), any(), eq(3))
    }

    @Test
    fun getCurrentUserRepoList_noLoggedInUser() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.error(AuthenticationException()))

        mUsecase.getCurrentUserRepoList(ListEntity(), FilterOptions())
                .test()
                .assertError(AuthenticationException::class.java)
        verifyZeroInteractions(mRepoRepository)
    }

    @Test
    fun getCurrentUserRepoList_hasLoggedInUser_getError() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("abcd"))
        whenever(mRepoRepository.getMyUserRepoList(any(), any(), any()))
                .thenReturn(Single.error(Exception("error")))

        mUsecase.getCurrentUserRepoList(ListEntity(), FilterOptions())
                .test()
                .assertErrorMessage("error")

        verify(mRepoRepository).getMyUserRepoList(eq("abcd"), any(), eq(ListEntity.STARTING_PAGE))
    }

    @Test
    fun getCurrentUserRepoList_hasLoggedInUser_getSuccess() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("duyp"))
        whenever(mRepoRepository.getMyUserRepoList(any(), any(), any()))
                .thenReturn(Single.just(ListEntity()))

        mUsecase.getCurrentUserRepoList(ListEntity(next = 3), FilterOptions())
                .test()
                .assertNoErrors()
                .assertValueCount(1)

        verify(mRepoRepository).getMyUserRepoList(eq("duyp"), any(), eq(3))
    }

}
package com.duyp.architecture.clean.android.powergit.data.repositories

import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.database.RepoDao
import com.duyp.architecture.clean.android.powergit.data.entities.pagination.PageableApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoApiData
import com.duyp.architecture.clean.android.powergit.data.entities.repo.RepoLocalData
import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepoRepositoryImplTest {

    @Mock private lateinit var mUserService: UserService

    @Mock private lateinit var mRepoDao: RepoDao

    private lateinit var mRepoRepository: RepoRepositoryImpl

    @Before
    fun setup() {
        mRepoRepository = RepoRepositoryImpl(mRepoDao, mUserService)
    }

    @Test
    fun getUserRepoList_errorFirstPage_shouldReturnLocalData() {
        whenever(mUserService.getRepos(any(), any(), any()))
                .thenReturn(Single.error(Exception()))
        whenever(mRepoDao.getUserRepos(any())).thenReturn(Single.just(listOf(
                RepoLocalData(1), RepoLocalData(2), RepoLocalData(3)
        )))

        mRepoRepository.getUserRepoList("duyp", FilterOptions(), ListEntity.STARTING_PAGE)
                .test()
                .assertValue {
                    it.items.size == 3 && it.isOfflineData && it.apiError is Exception
                }
        verify(mUserService).getRepos(eq("duyp"), any(), eq(ListEntity.STARTING_PAGE))
        verify(mRepoDao).getUserRepos("duyp")
    }

    @Test
    fun getUserRepoList_errorNotFirstPage_shouldNotReturnLocalData() {
        whenever(mUserService.getRepos(any(), any(), any()))
                .thenReturn(Single.error(Exception()))

        mRepoRepository.getUserRepoList("duyp", FilterOptions(), ListEntity.STARTING_PAGE + 1)
                .test()
                .assertNoValues()
                .assertError(Exception::class.java)

        verify(mUserService).getRepos(eq("duyp"), any(), eq(ListEntity.STARTING_PAGE + 1))
        verifyZeroInteractions(mRepoDao)
    }

    @Test
    fun getUserRepoList_success_shouldSaveIntoDatabase() {
        whenever(mUserService.getRepos(any(), any(), any()))
                .thenReturn(Single.just(PageableApiData(items = listOf(
                        RepoApiData(1),
                        RepoApiData(2),
                        RepoApiData(3)
                ))))

        mRepoRepository.getUserRepoList("duyp", FilterOptions(), 1)
                .test()
                .assertValue {
                    it.items.size == 3 && !it.isOfflineData && it.apiError == null
                }

        verify(mUserService).getRepos(eq("duyp"), any(), eq(1))
        verify(mRepoDao).insertList(argThat { size == 3})
    }

    @Test
    fun getMyUserRepoList_errorFirstPage_shouldReturnLocalData() {
        whenever(mUserService.getMyRepos(any(), any()))
                .thenReturn(Single.error(Exception()))
        whenever(mRepoDao.getUserRepos(any())).thenReturn(Single.just(listOf(
                RepoLocalData(1), RepoLocalData(2), RepoLocalData(3)
        )))

        mRepoRepository.getMyUserRepoList("duyp", FilterOptions(), ListEntity.STARTING_PAGE)
                .test()
                .assertValue {
                    it.items.size == 3 && it.isOfflineData && it.apiError is Exception
                }
        verify(mUserService).getMyRepos(any(), eq(ListEntity.STARTING_PAGE))
        verify(mRepoDao).getUserRepos("duyp")
    }

    @Test
    fun getMyUserRepoList_errorNotFirstPage_shouldNotReturnLocalData() {
        whenever(mUserService.getMyRepos(any(), any()))
                .thenReturn(Single.error(Exception()))

        mRepoRepository.getMyUserRepoList("duyp1", FilterOptions(), ListEntity.STARTING_PAGE + 2)
                .test()
                .assertNoValues()
                .assertError(Exception::class.java)

        verify(mUserService).getMyRepos(any(), eq(ListEntity.STARTING_PAGE + 2))
        verifyZeroInteractions(mRepoDao)
    }

    @Test
    fun getMyUserRepoList_success_shouldSaveIntoDatabase() {
        whenever(mUserService.getMyRepos(any(), any()))
                .thenReturn(Single.just(PageableApiData(items = listOf(
                        RepoApiData(1),
                        RepoApiData(2)
                ))))

        mRepoRepository.getMyUserRepoList("duyp", FilterOptions(), 1)
                .test()
                .assertValue {
                    it.items.size == 2 && !it.isOfflineData && it.apiError == null
                }

        verify(mUserService).getMyRepos(any(), eq(1))
        verify(mRepoDao).insertList(argThat { size == 2 })
    }


}
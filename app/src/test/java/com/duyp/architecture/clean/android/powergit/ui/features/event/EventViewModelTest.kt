package com.duyp.architecture.clean.android.powergit.ui.features.event

import ViewModelTest
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetUserEventList
import com.duyp.architecture.clean.android.powergit.ui.base.ListIntent
import com.duyp.architecture.clean.android.powergit.ui.base.ListState
import com.duyp.architecture.clean.android.powergit.utils.*
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock

class EventViewModelTest: ViewModelTest<ListState, ListIntent, EventViewModel>() {

    @Mock private lateinit var mGetUserEventList: GetUserEventList

    override fun createViewModel(): EventViewModel {
        return EventViewModel(mGetUserEventList)
    }

    @Test
    fun startup_shouldForceRefresh() {
        processIntents()
        viewState().assertValue { refresh.assertNotNull() }
    }

    @Test
    fun startup_shouldNotForceRefreshTwiceIfScreenRotated() {
        processIntents()
        viewState().assertValue {
            refresh.assertNotNullAndNotHandledYet()
        }
        mViewModel.state.value?.refresh?.get {  }

        processIntents()
        viewState().assertValue { refresh.assertNotNullAndHandled() }
    }

    @Test
    fun refresh_shouldNotLoadIfIsLoading() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.create {
                    // never complete
                })
        intent(ListIntent.Refresh)

        viewState().assertValue { showLoading }

        // refresh more 4 times
        intent(ListIntent.Refresh)
        intent(ListIntent.Refresh)
        intent(ListIntent.Refresh)
        intent(ListIntent.Refresh)

        // verify that only load data 1 time
        verify(mGetUserEventList, times(1)).getMyUserEvents(any(), any())
    }

    @Test
    fun refresh_shouldBeAbleToRefreshIfPreviousError() {
        var emitter: SingleEmitter<ListEntity<EventEntity>>? = null
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.create {
                    emitter = it
                })
        intent(ListIntent.Refresh)

        viewState().assertValue { showLoading }

        // load done with error, should be able to load again
        emitter?.onError(Exception("error"))

        viewState().assertValue { !showLoading && errorMessage.assertContent("error") }

        // refresh again
        intent(ListIntent.Refresh)

        viewState().assertValue { showLoading }

        // verify that we are able to load data for the 2nd refresh, means 2 times load
        verify(mGetUserEventList, times(2)).getMyUserEvents(any(), any())
    }

    @Test
    fun refresh_errorShouldShowEmptyViewAndErrorMessage() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any())).thenReturn(Single.error(Exception("error message")))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && showEmptyView && !showOfflineNotice
                            && errorMessage.assertContent("error message") && !requireLogin
                }
                .withPrevious {
                    showLoading && !showEmptyView && !showOfflineNotice  && !requireLogin
                }
    }

    @Test
    fun refresh_authenticationError_shouldRequireLogin() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.error(AuthenticationException("session expired")))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && !showOfflineNotice && showEmptyView
                            && errorMessage.assertContent("session expired") && requireLogin
                }
                .withPrevious {
                    showLoading && !showOfflineNotice && !showEmptyView && !requireLogin
                }
    }

    @Test
    fun refresh_successWithOfflineData() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(isOfflineData = true, apiError = Exception("error"))))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && loadCompleted.assertNotNull()
                            && showOfflineNotice && errorMessage.assertContent("error")
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView }
    }

    @Test
    fun refresh_successWithApiData() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(isOfflineData = false)))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && loadCompleted.assertNotNull() && !showOfflineNotice
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView }
    }


    @Test
    fun refresh_successWithEmptyData_shouldShowEmptyView() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity()))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && loadCompleted.assertNotNull() && showEmptyView
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView }
    }

    @Test
    fun refresh_successWithData() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(
                        isOfflineData = false,
                        items = listOf(EventEntity(id = 1), EventEntity(id = 2))
                )))
        intent(ListIntent.Refresh)

        viewState()
                .assertValue {
                    !showLoading && !showEmptyView && !showOfflineNotice && loadCompleted.assertContentNotNull()
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView }

        assertEquals(2, mViewModel.getTotalCount())
        assertEquals(1, mViewModel.getItemAtPosition(0)!!.id)
        assertEquals(2, mViewModel.getItemAtPosition(1)!!.id)
    }

    @Test
    fun loadMore_canNotDoWithoutRefreshing() {
        intent(ListIntent.LoadMore)
        viewState().assertValue { !showLoading }
        verifyZeroInteractions(mGetUserEventList)
    }

    @Test
    fun loadMore_shouldPassLoadingMoreEvent() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(
                        next = 2, last = 3, items = listOf(EventEntity(id = 1), EventEntity(id = 2))
                )))
        intent(ListIntent.Refresh)

        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity()))
        intent(ListIntent.LoadMore)

        viewState()
                .assertValue { loadCompleted.assertNotNull() }
                .withPrevious { showLoading }
                .withPrevious { loadingMore.assertNotNull() }
    }


}
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
        viewState().assertLastValue { refresh.assertNotNull() }
    }

    @Test
    fun startup_shouldNotForceRefreshTwiceIfScreenRotated() {
        processIntents()
        viewState().assertLastValue {
            refresh.assertNotNullAndNotHandledYet()
        }
        mViewModel.state.value?.refresh?.get {  }

        processIntents()
        viewState().assertLastValue { refresh.assertNotNullAndHandled() }
    }

    @Test
    fun refresh_shouldNotLoadIfIsLoading() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.create {
                    // never complete
                })
        intent(ListIntent.Refresh)

        viewState().assertLastValue { showLoading }

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

        viewState().assertLastValue { showLoading }

        // load done with error, should be able to load again
        emitter?.onError(Exception("error"))

        viewState().assertLastValue { !showLoading && errorMessage.assertContent("error") && refreshable }

        // refresh again
        intent(ListIntent.Refresh)

        viewState().assertLastValue { showLoading }

        // verify that we are able to load data for the 2nd refresh, means 2 times load
        verify(mGetUserEventList, times(2)).getMyUserEvents(any(), any())
    }

    @Test
    fun refresh_errorShouldShowEmptyViewAndErrorMessage() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any())).thenReturn(Single.error(Exception("error message")))
        intent(ListIntent.Refresh)

        viewState()
                .assertLastValue {
                    !showLoading && showEmptyView && !showOfflineNotice
                            && errorMessage.assertContent("error message") && !requireLogin && refreshable
                }
                .withPrevious {
                    showLoading && !showEmptyView && !showOfflineNotice  && !requireLogin
                }
    }

    @Test
    fun refresh_authenticationError_shouldRequireLoginAndDisableRefresh() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.error(AuthenticationException("session expired")))
        intent(ListIntent.Refresh)

        viewState()
                .assertLastValue {
                    !showLoading && !showOfflineNotice && showEmptyView
                            && errorMessage.assertContent("session expired") && requireLogin && !refreshable
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
                .assertLastValue {
                    !showLoading && loadCompleted.assertNotNull()
                            && showOfflineNotice && errorMessage.assertContent("error")
                }
                // still show offline to prevent ui glitch
                .withPrevious { showLoading && showOfflineNotice && !showEmptyView }
    }

    @Test
    fun refresh_successWithApiData() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(isOfflineData = false)))
        intent(ListIntent.Refresh)

        viewState()
                .assertLastValue {
                    !showLoading && loadCompleted.assertNotNull() && !showOfflineNotice && refreshable
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView && !refreshable }
    }


    @Test
    fun refresh_successWithEmptyData_shouldShowEmptyView() {
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity()))
        intent(ListIntent.Refresh)

        viewState()
                .assertLastValue {
                    !showLoading && loadCompleted.assertNotNull() && showEmptyView && refreshable
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView &&!refreshable }
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
                .assertLastValue {
                    !showLoading && !showEmptyView && !showOfflineNotice && loadCompleted.assertContentNotNull() && refreshable
                }
                .withPrevious { showLoading && !showOfflineNotice && !showEmptyView }

        assertEquals(2, mViewModel.getTotalCount())
        assertEquals(1, mViewModel.getItemAtPosition(0)!!.id)
        assertEquals(2, mViewModel.getItemAtPosition(1)!!.id)
    }

    @Test
    fun loadMore_canNotDoWithoutRefreshing() {
        intent(ListIntent.LoadMore)
        viewState().assertLastValue { !showLoading }
        verifyZeroInteractions(mGetUserEventList)
    }

    @Test
    fun loadMore_shouldPassLoadingMoreEvent() {
        val firstList = ListEntity(
                next = 2, last = 3, items = listOf(EventEntity(id = 1), EventEntity(id = 2))
        )
        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(firstList))
        intent(ListIntent.Refresh)

        whenever(mGetUserEventList.getMyUserEvents(any(), any()))
                .thenReturn(Single.just(firstList.mergeWith(ListEntity())))

        intent(ListIntent.LoadMore)

        viewState()
                // load more completed
                .assertLastValue { loadCompleted.assertNotNull() && !showEmptyView && !showLoading && refreshable }
                // data updated for load more
                .withPrevious { dataUpdated.assertNotNull() && !refreshable && errorMessage == null && !showEmptyView}
                // we don't show loading if is loading more pages
                .withPrevious { !showLoading }
                // start loading more
                .withPrevious { loadingMore.assertNotNull() }
                // first load completed
                .withPrevious { loadCompleted.assertNotNull() && !showEmptyView && !showLoading && refreshable }
                // data updated with first load
                .withPrevious { dataUpdated.assertNotNull() }
                // first show loading for refresh
                .withPrevious { showLoading && !showEmptyView && !requireLogin && !refreshable }
    }
}
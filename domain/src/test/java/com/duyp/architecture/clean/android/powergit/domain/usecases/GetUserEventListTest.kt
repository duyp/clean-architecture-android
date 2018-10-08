package com.duyp.architecture.clean.android.powergit.domain.usecases

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.exception.AuthenticationException
import com.duyp.architecture.clean.android.powergit.domain.repositories.EventRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mock

class GetUserEventListTest: UseCaseTest<GetUserEventList>() {

    @Mock private lateinit var mGetUser: GetUser

    @Mock private lateinit var mEventRepository: EventRepository

    override fun createUseCase(): GetUserEventList {
        return  GetUserEventList(mEventRepository, mGetUser)
    }

    @Test
    fun getUserEvents_selfEvent_error() {
        whenever(mEventRepository.getUserEvents(any(), any())).thenReturn(Single.error(Exception()))

        mUsecase.getUserEvents(ListEntity(), "duyp", false)
                .test()
                .assertError(Exception::class.java)
        verify(mEventRepository).getUserEvents("duyp", ListEntity.STARTING_PAGE)
    }

    @Test
    fun getUserEvents_selfEvent_success() {
        whenever(mEventRepository.getUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(next = 2, items = listOf(
                        EventEntity(id = 1),
                        EventEntity(id = 2),
                        EventEntity(id = 3),
                        EventEntity(id = 4)
                ))))

        mUsecase.getUserEvents(ListEntity(next = 1), "duyp", false)
                .test()
                .assertValue {
                    it.getNextPage() == 2 && it.items.size == 4
                }

        verify(mEventRepository).getUserEvents("duyp", 1)
    }

    @Test
    fun getUserEvents_selfEvent_success_shouldMergeWithPrevious() {
        whenever(mEventRepository.getUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(next = 3, items = listOf(
                        EventEntity(id = 4),
                        EventEntity(id = 5),
                        EventEntity(id = 6),
                        EventEntity(id = 7)
                ))))

        mUsecase.getUserEvents(ListEntity(next = 2, items = listOf(
                EventEntity(id = 1),
                EventEntity(id = 2),
                EventEntity(id = 3)
        )), "duyp", false)
                .test()
                .assertValue {
                    it.getNextPage() == 3 && it.items.size == 7
                }

        verify(mEventRepository).getUserEvents("duyp", 2)
    }

    @Test
    fun getUserEvents_selfEvent_success_fistPage_shouldNotMergeWithPrevious() {
        whenever(mEventRepository.getUserEvents(any(), any()))
                .thenReturn(Single.just(ListEntity(next = 2, first = 1, items = listOf(
                        EventEntity(id = 4),
                        EventEntity(id = 5),
                        EventEntity(id = 6),
                        EventEntity(id = 7)
                ))))

        mUsecase.getUserEvents(ListEntity(next = 1, items = listOf(
                EventEntity(id = 1),
                EventEntity(id = 2),
                EventEntity(id = 3)
        )), "duyp", false)
                .test()
                .assertValue {
                    it.getNextPage() == 2 && it.items.size == 4
                }

        verify(mEventRepository).getUserEvents("duyp", 1)
    }

    @Test
    fun getUserEvents_receivedEvent_error() {
        whenever(mEventRepository.getUserReceivedEvents(any(), any())).thenReturn(Single.error(Exception()))

        mUsecase.getUserEvents(ListEntity(next = 10), "duyp1", true)
                .test()
                .assertError(Exception::class.java)
        verify(mEventRepository).getUserReceivedEvents("duyp1", 10)
    }

    @Test
    fun getUserEvents_receivedEvent_success() {
        whenever(mEventRepository.getUserReceivedEvents(any(), any())).thenReturn(Single.just(ListEntity()))

        mUsecase.getUserEvents(ListEntity(next = 12), "duyp1", true)
                .test()
                .assertValueCount(1)

        verify(mEventRepository).getUserReceivedEvents("duyp1", 12)
    }

    @Test
    fun getMyUserEvents_noLoggedInUser() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.error(AuthenticationException()))

        mUsecase.getMyUserEvents(ListEntity(), true)
                .test()
                .assertError(AuthenticationException::class.java)
        verifyZeroInteractions(mEventRepository)
    }

    @Test
    fun getMyUserEvents_hasLoggedInUser_getSelfEvent_error() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("duyp"))
        whenever(mEventRepository.getUserEvents(any(), any())).thenReturn(Single.error(Exception()))

        mUsecase.getMyUserEvents(ListEntity(), false)
                .test()
                .assertError(Exception::class.java)

        verify(mEventRepository).getUserEvents("duyp", ListEntity.STARTING_PAGE)
    }

    @Test
    fun getMyUserEvents_hasLoggedInUser_getSelfEvent_success() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("duyp1"))
        whenever(mEventRepository.getUserEvents(any(), any())).thenReturn(Single.just(ListEntity()))

        mUsecase.getMyUserEvents(ListEntity(), false)
                .test()
                .assertNoErrors()

        verify(mEventRepository).getUserEvents("duyp1", ListEntity.STARTING_PAGE)
    }

    @Test
    fun getMyUserEvents_hasLoggedInUser_getReceivedEvent_error() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("duyp"))
        whenever(mEventRepository.getUserReceivedEvents(any(), any())).thenReturn(Single.error(Exception()))

        mUsecase.getMyUserEvents(ListEntity(), true)
                .test()
                .assertError(Exception::class.java)

        verify(mEventRepository).getUserReceivedEvents("duyp", ListEntity.STARTING_PAGE)
    }

    @Test
    fun getMyUserEvents_hasLoggedInUser_getReceivedEvent_success() {
        whenever(mGetUser.getCurrentLoggedInUsername()).thenReturn(Single.just("duyp1"))
        whenever(mEventRepository.getUserReceivedEvents(any(), any())).thenReturn(Single.just(ListEntity()))

        mUsecase.getMyUserEvents(ListEntity(), true)
                .test()
                .assertNoErrors()

        verify(mEventRepository).getUserReceivedEvents("duyp1", ListEntity.STARTING_PAGE)
    }

    @Test
    fun getUserEvents() {
    }
}
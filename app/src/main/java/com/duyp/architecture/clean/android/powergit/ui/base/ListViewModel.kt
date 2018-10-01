package com.duyp.architecture.clean.android.powergit.ui.base

import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.printStacktraceIfDebug
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

abstract class ListViewModel<S, I: ListIntent, T>: BaseViewModel<S, I>(), AdapterData<T> {

    private var mListEntity: ListEntity<T>? = null

    private var mIsLoading: Boolean = false

    override fun composeIntent(intentSubject: Observable<I>) {
        withListState {
            // only refresh one time (no refresh if UI get rotated)
            if (refresh == null) {
                setListState { copy(refresh = Event.empty()) }
            }
        }

        addDisposable {
            intentSubject.ofType(getRefreshIntent()::class.java)
                    .subscribeOn(Schedulers.io())
                    .filter { !mIsLoading }
                    .switchMap { loadPage(ListEntity.STARTING_PAGE) }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(getLoadMoreIntent()::class.java)
                    .subscribeOn(Schedulers.io())
                    .filter { !mIsLoading && (mListEntity?.canLoadMore() ?: false) }
                    .doOnNext {
                        setListState { copy(loadingMore = Event.empty()) }
                    }
                    .switchMap { loadPage(mListEntity!!.next) }
                    .subscribe()
        }
    }

    override fun getTotalCount(): Int = mListEntity?.items?.size ?: 0

    override fun getItemAtPosition(position: Int): T? {
        if (mListEntity == null || mListEntity!!.items.isEmpty() || position < 0 || position >= getTotalCount())
            return null
        return mListEntity!!.items[position]
    }

    protected abstract fun setListState(s: ListState.() -> ListState)

    protected abstract fun withListState(s: ListState.() -> Unit)

    protected abstract fun loadPageObservable(page: Int): Observable<ListEntity<T>>

    abstract fun getRefreshIntent(): I

    abstract fun getLoadMoreIntent(): I

    private fun loadPage(page: Int): Observable<ListEntity<T>> {
        return loadPageObservable(page)
                .doOnSubscribe {
                    mIsLoading = true
                    setListState { copy(showLoading = page == ListEntity.STARTING_PAGE) }
                }
                .doOnNext {
                    mListEntity = it
                    mIsLoading = false
                    setListState {
                        copy(
                                showLoading = false,
                                loadCompleted = Event.empty(),
                                showOfflineNotice = !mListEntity!!.isApiData
                        )
                    }
                }
                .doOnError {
                    it.printStacktraceIfDebug()
                    mIsLoading = false
                    setListState { copy(showLoading = false, errorMessage = Event(it.message ?: "")) }
                }
                .onErrorResumeNext { _: Throwable -> Observable.empty() }
    }
}

data class ListState(
        val showLoading: Boolean = false,
        val showOfflineNotice: Boolean = false,
        val refresh: Event<Unit>? = null,
        val errorMessage: Event<String>? = null,
        val loadingMore: Event<Unit>? = null,
        val loadCompleted: Event<Unit>? = null
)

interface ListIntent {
    object RefreshIntent: ListIntent
    object LoadMoreIntent: ListIntent
}
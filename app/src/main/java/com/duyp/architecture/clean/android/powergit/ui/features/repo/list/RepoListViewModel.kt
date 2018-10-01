package com.duyp.architecture.clean.android.powergit.ui.features.repo.list

import com.duyp.architecture.clean.android.powergit.Event
import com.duyp.architecture.clean.android.powergit.domain.entities.FilterOptions
import com.duyp.architecture.clean.android.powergit.domain.entities.ListEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.repo.RepoEntity
import com.duyp.architecture.clean.android.powergit.domain.usecases.repo.GerUserRepoList
import com.duyp.architecture.clean.android.powergit.printStacktraceIfDebug
import com.duyp.architecture.clean.android.powergit.ui.base.BaseViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepoListViewModel @Inject constructor(
        private val mGetUserRepoList: GerUserRepoList
): BaseViewModel<RepoListState, RepoListIntent>() {

    private val mFilterOptions = FilterOptions()

    private var mListEntity: ListEntity<RepoEntity>? = null

    init {
        setState { RepoListState(refresh = Event(true)) }
    }

    override fun initState() = RepoListState()

    override fun composeIntent(intentSubject: Observable<RepoListIntent>) {
        addDisposable {
            intentSubject.ofType(RepoListIntent.RefreshIntent::class.java)
                    .subscribeOn(Schedulers.io())
                    .filter { !state().isLoading }
                    .switchMap { loadPage(1, mFilterOptions) }
                    .subscribe()
        }

        addDisposable {
            intentSubject.ofType(RepoListIntent.LoadMoreIntent::class.java)
                    .subscribeOn(Schedulers.io())
                    .filter { mListEntity != null && !state().isLoading }
                    .filter { mListEntity!!.canLoadMore() }
                    .doOnNext {
                        setState { copy(onLoadingMore = Event.empty()) }
                    }
                    .switchMap { loadPage(mListEntity!!.next, mFilterOptions) }
                    .subscribe()
        }
    }

    fun totalItemCount(): Int = mListEntity?.items?.size ?: 0

    fun getItemAtPosition(position: Int): RepoEntity? {
        if (mListEntity == null || mListEntity!!.items.isEmpty() || position < 0 || position >= totalItemCount())
            return null
        return mListEntity!!.items[position]
    }

    private fun loadPage(page: Int, filterOptions: FilterOptions) =
            mGetUserRepoList.getRepoList("duyp", filterOptions, page)
                    .doOnSubscribe {
                        setState { copy(isLoading = true) }
                    }
                    .doOnSuccess {
                        mListEntity = it
                        setState {
                            copy (
                                    isLoading = false,
                                    onLoadCompleted = Event.empty(),
                                    showOfflineNotice = !mListEntity!!.isApiData
                            )
                        }
                    }
                    .doOnError {
                        it.printStacktraceIfDebug()
                        setState { copy(isLoading = false, errorMessage = Event(it.message ?: "")) }
                    }
                    .toObservable()
                    .onErrorResumeNext { _: Throwable -> Observable.empty() }
}

data class RepoListState(
        val isLoading: Boolean = false,
        val showOfflineNotice: Boolean = false,
        val refresh: Event<Boolean>? = null, // true as show loading
        val errorMessage: Event<String>? = null,
        val onLoadingMore: Event<Unit>? = null,
        val onLoadCompleted: Event<Unit>? = null
)

sealed class RepoListIntent {
    object RefreshIntent: RepoListIntent()
    object LoadMoreIntent: RepoListIntent()
}

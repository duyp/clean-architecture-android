package com.duyp.architecture.clean.android.powergit.ui.base

/**
 * Basic version if [ListViewModel] where the view only need to show a list of data without any other intents and
 * states except [ListIntent] and [ListState].
 *
 * So basically, the view state is enforced to [ListState] and the view intent is enforced to [ListIntent], other
 * things remain the same in [ListViewModel].
 */
abstract class BasicListViewModel<EntityType>: ListViewModel<ListState, ListIntent, EntityType>() {

    override fun initState() = ListState()

    override fun setListState(s: ListState.() -> ListState) {
        setState(s)
    }

    override fun withListState(s: ListState.() -> Unit) {
        withState(s)
    }

    override fun getRefreshIntent() = ListIntent.Refresh

    override  fun getLoadMoreIntent() = ListIntent.LoadMore
}
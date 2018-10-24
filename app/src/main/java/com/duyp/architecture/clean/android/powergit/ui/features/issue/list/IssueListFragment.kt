package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.addSimpleOnItemSelectedListener
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.getEnum
import com.duyp.architecture.clean.android.powergit.setDefaultElevation
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_REPO_NAME
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_TYPE
import com.duyp.architecture.clean.android.powergit.ui.BundleConstants.EXTRA_USERNAME
import com.duyp.architecture.clean.android.powergit.ui.base.ListFragment
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.AdapterData
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import kotlinx.android.synthetic.main.fragment_user_issue_list.*
import javax.inject.Inject

class IssueListFragment: ListFragment<IssueEntity, IssueAdapter, IssueListIntent, IssueListState,
        IssueListViewModel>() {

    @Inject internal lateinit var mAvatarLoader: AvatarLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val type = arguments!!.getEnum<IssueListType>(EXTRA_TYPE)
            if (type == IssueListType.REPO_ISSUES) {
                mViewModel.initWithRepo(arguments!![EXTRA_USERNAME].toString(), arguments!![EXTRA_REPO_NAME].toString())
            } else {
                mViewModel.initWithUser(arguments!![EXTRA_USERNAME]?.toString())
            }
        }
    }

    override fun createAdapter(data: AdapterData<IssueEntity>): IssueAdapter {
        return IssueAdapter(
                data = data,
                avatarLoader = mAvatarLoader,
                showRepoName = mViewModel.listType != IssueListType.REPO_ISSUES
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        header.setDefaultElevation(true)

        if (mViewModel.listType == IssueListType.USER_ISSUES) {
            // spinner
            ArrayAdapter(
                    context,
                    R.layout.item_my_issue_spinner,
                    MyIssueType.listStringIds().map { context!!.getString(it) }
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner.adapter = adapter
                typeSpinner.addSimpleOnItemSelectedListener { _, _, position, _ ->
                    onIntent(IssueListIntent.MyIssueTypeSelected(MyIssueType.of(position)))
                }
            }
        }

        // open / closed state
        stateSwitcher.setOnSwitchListener { onIntent(IssueListIntent.StateSwitch(it)) }

        withState {
            onListStateUpdated(listState)
            typeSpinner.setSelection(myIssueType.getPosition())
            stateSwitcher.setState(isOpenIssue)
        }
    }

    override fun getLayoutResource() = R.layout.fragment_user_issue_list

    override fun updateOfflineNotice(showIt: Boolean) {
        super.updateOfflineNotice(showIt)
        header.setDefaultElevation(!showIt)
    }
}
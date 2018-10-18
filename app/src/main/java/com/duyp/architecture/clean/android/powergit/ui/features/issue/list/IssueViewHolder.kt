package com.duyp.architecture.clean.android.powergit.ui.features.issue.list

import android.graphics.Color
import android.support.v4.graphics.ColorUtils
import android.support.v7.widget.AppCompatImageView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.domain.entities.IssueEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.IssueState
import com.duyp.architecture.clean.android.powergit.inflate
import com.duyp.architecture.clean.android.powergit.ui.base.adapter.BaseViewHolder
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import com.duyp.architecture.clean.android.powergit.ui.utils.ParseDateFormat
import com.duyp.architecture.clean.android.powergit.ui.utils.UiUtils
import com.duyp.architecture.clean.android.powergit.ui.widgets.AutoLinearLayout
import com.duyp.architecture.clean.android.powergit.ui.widgets.SpannableBuilder

/**
 * Created by duypham on 10/30/17.
 *
 */

class IssueViewHolder private constructor(
        itemView: View, 
        private val avatarLoader: AvatarLoader, 
        private val withAvatar: Boolean,
        private val showRepoName: Boolean,
        private val showState: Boolean = false
) : BaseViewHolder<IssueEntity>(itemView) {

    private var avatarLayout: ImageView? = itemView.findViewById(R.id.avatarLayout)
    private var issueState: AppCompatImageView? = itemView.findViewById(R.id.issue_state)
    
    private var title: TextView = itemView.findViewById(R.id.title)
    private var details: TextView = itemView.findViewById(R.id.details)
    private var commentsNo: TextView = itemView.findViewById(R.id.commentsNo)
    private var labelContainer: AutoLinearLayout = itemView.findViewById(R.id.labelContainer)

    private var labelMarginVertical = itemView.context.resources.getDimensionPixelOffset(R.dimen.one)
    private var labelMarginHorizontal = itemView.context.resources.getDimensionPixelOffset(R.dimen.xx_tiny)
    private var labelSpacing = itemView.context.resources.getDimensionPixelOffset(R.dimen.xxx_tiny)

    override fun bindData(data: IssueEntity) {
        title.text = data.title
        if (data.state != null) {
            val builder = SpannableBuilder.builder()
            if (showRepoName && data.repoName != null && data.repoOwner != null) {
                builder.bold(data.repoOwner)
                        .append("/")
                        .bold(data.repoName)
                        .bold("#")
                        .bold(data.number.toString()).append(" ")
                        .append(" ")
            }
            if (!showRepoName) {
                if (data.state == IssueState.CLOSED) {
                    if (data.closedBy == null) {
                        builder.bold("#")
                                .bold(data.number.toString()).append(" ")
                                .append(" ")
                    } else {
                        builder.append("#")
                                .append(data.number.toString()).append(" ")
                                .append(data.closedBy!!.login)
                                .append(" ")
                    }
                } else {
                    builder.bold("#")
                            .bold(data.number.toString()).append(" ")
                            .append(data.user!!.login)
                            .append(" ")
                }
            }
            val time = ParseDateFormat.getTimeAgo(if (data.state == IssueState.OPEN)
                data.createdAt
            else
                data.closedAt)
            details.text = builder
                    .append(data.state)
                    .append(" ")
                    .append(time)
            if (data.comments > 0) {
                commentsNo.text = data.comments.toString()
                commentsNo.visibility = View.VISIBLE
            } else {
                commentsNo.visibility = View.GONE
            }
        }
        if (showState) {
            issueState!!.visibility = View.VISIBLE
            issueState!!.setImageResource(if (data.state == IssueState.OPEN)
                R.drawable.ic_issue_opened_small
            else
                R.drawable.ic_issue_closed_small)
        } else {
            issueState!!.visibility = View.GONE
        }
        if (withAvatar && avatarLayout != null && data.user != null) {
            avatarLoader.loadImage<String>(data.user!!.avatarUrl, avatarLayout)
            avatarLayout!!.visibility = View.VISIBLE
        }

        labelContainer.removeAllViews()
        if (data.labels != null) {
            val sortedLabels = data.labels!!.sortedByDescending { it.name?.length }
            labelContainer.visibility = if (sortedLabels.isEmpty()) View.GONE else View.VISIBLE
            val context = labelContainer.context
            for ((_, _, name, color) in sortedLabels) {
                val textView = TextView(context)
                val bgColor = Color.parseColor("#" + color!!)
                val textColor = if (ColorUtils.calculateLuminance(bgColor) < 0.5) Color.WHITE else Color.BLACK
                textView.text = name
                textView.setTextColor(textColor)
                textView.background = UiUtils.getTintedDrawableFromColor(
                        context.resources.getDrawable(R.drawable.bg_label),
                        bgColor
                )
                textView.gravity = Gravity.CENTER
                textView.setPadding(labelMarginHorizontal, 0, labelMarginHorizontal, labelMarginVertical)

                val params = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(0, labelSpacing, labelSpacing, 0)

                labelContainer.addView(textView, params)
            }
        } else {
            labelContainer.visibility = View.GONE
        }
    }

    companion object {

        @JvmOverloads
        fun newInstance(viewGroup: ViewGroup, avatarLoader: AvatarLoader, withAvatar: Boolean, showRepoName: Boolean, 
                        showState: Boolean = false): IssueViewHolder {
            return if (withAvatar) {
                IssueViewHolder(viewGroup.inflate(R.layout.item_issue), avatarLoader, true, showRepoName, showState)
            } else {
                IssueViewHolder(viewGroup.inflate(R.layout.item_issue_no_image), avatarLoader, false, showRepoName, showState)
            }
        }
    }
}

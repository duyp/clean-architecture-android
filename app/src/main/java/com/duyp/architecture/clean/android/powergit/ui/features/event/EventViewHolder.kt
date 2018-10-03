package com.duyp.architecture.clean.android.powergit.ui.features.event

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import com.duyp.architecture.clean.android.powergit.domain.entities.type.EventType.*
import com.duyp.architecture.clean.android.powergit.inflate
import com.duyp.architecture.clean.android.powergit.ui.provider.markdown.MarkDownProvider
import com.duyp.architecture.clean.android.powergit.ui.utils.AvatarLoader
import com.duyp.architecture.clean.android.powergit.ui.utils.ParseDateFormat
import com.duyp.architecture.clean.android.powergit.ui.widgets.SpannableBuilder

/**
 * Created by Kosh on 11 Nov 2016, 2:08 PM
 *
 * Modifed by Duy Pham Oct 2018
 */

//private final NavigatorHelper navigatorHelper;

class EventViewHolder private constructor(
        itemView: View,
        private val avatarLoader: AvatarLoader
) : RecyclerView.ViewHolder(itemView) {

    internal var avatar: ImageView? = itemView.findViewById(R.id.avatarLayout)
    internal var description: TextView? = itemView.findViewById(R.id.description)
    internal var title: TextView? = itemView.findViewById(R.id.title)
    internal var date: TextView? = itemView.findViewById(R.id.date)

    private val resources: Resources = itemView.resources

    fun bind(eventsModel: EventEntity) {
        appendAvatar(eventsModel)
        val spannableBuilder = SpannableBuilder.builder()
        appendActor(eventsModel, spannableBuilder)
        description!!.maxLines = 2
        description!!.text = ""
        description!!.visibility = View.GONE
        if (eventsModel.type != null) {
            val type = eventsModel.type
            when (type) {
                WatchEvent -> appendWatch(spannableBuilder, eventsModel)
                CreateEvent -> appendCreateEvent(spannableBuilder, eventsModel)
                CommitCommentEvent -> appendCommitComment(spannableBuilder, eventsModel)
                DownloadEvent -> appendDownloadEvent(spannableBuilder, eventsModel)
                FollowEvent -> appendFollowEvent(spannableBuilder, eventsModel)
                ForkEvent -> appendForkEvent(spannableBuilder, eventsModel)
                GistEvent -> appendGistEvent(spannableBuilder, eventsModel)
                GollumEvent -> appendGollumEvent(spannableBuilder, eventsModel)
                IssueCommentEvent -> appendIssueCommentEvent(spannableBuilder, eventsModel)
                IssuesEvent -> appendIssueEvent(spannableBuilder, eventsModel)
                MemberEvent -> appendMemberEvent(spannableBuilder, eventsModel)
                PublicEvent -> appendPublicEvent(spannableBuilder, eventsModel)
                PullRequestEvent -> appendPullRequestEvent(spannableBuilder, eventsModel)
                PullRequestReviewCommentEvent -> appendPullRequestReviewCommentEvent(spannableBuilder, eventsModel)
                PullRequestReviewEvent -> appendPullRequestReviewCommentEvent(spannableBuilder, eventsModel)
                RepositoryEvent -> appendPublicEvent(spannableBuilder, eventsModel)
                PushEvent -> appendPushEvent(spannableBuilder, eventsModel)
                TeamAddEvent -> appendTeamEvent(spannableBuilder, eventsModel)
                DeleteEvent -> appendDeleteEvent(spannableBuilder, eventsModel)
                ReleaseEvent -> appendReleaseEvent(spannableBuilder, eventsModel)
                ForkApplyEvent -> appendForkApplyEvent(spannableBuilder, eventsModel)
                OrgBlockEvent -> appendOrgBlockEvent(spannableBuilder, eventsModel)
                ProjectCardEvent -> appendProjectCardEvent(spannableBuilder, eventsModel, false)
                ProjectColumnEvent -> appendProjectCardEvent(spannableBuilder, eventsModel, true)
                OrganizationEvent -> appendOrganizationEvent(spannableBuilder, eventsModel)
                ProjectEvent -> appendProjectCardEvent(spannableBuilder, eventsModel, false)
            }
            date!!.gravity = Gravity.CENTER
            //date.setEventsIcon(type.getDrawableRes());
        }
        title!!.text = spannableBuilder
        date!!.text = ParseDateFormat.getTimeAgo(eventsModel.createdAt)
    }

    private fun appendOrganizationEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold(eventsModel.payload!!.action!!.replace("_".toRegex(), ""))
                .append(" ")
                .append(if (eventsModel.payload!!.invitation != null) eventsModel.payload!!.invitation!!.login!! + " " else "")
                .append(eventsModel.payload!!.organization!!.login)
    }

    private fun appendProjectCardEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity, isColumn: Boolean) {
        spannableBuilder.bold(eventsModel.payload!!.action)
                .append(" ")
                .append(if (!isColumn) "project" else "column")
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendOrgBlockEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold(eventsModel.payload!!.action)
                .append(" ")
                .append(eventsModel.payload!!.blockedUser!!.login)
                .append(" ")
                .append(eventsModel.payload!!.organization!!.login)
    }

    private fun appendForkApplyEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold(eventsModel.payload!!.head)
                .append(" ")
                .append(eventsModel.payload!!.before)
                .append(" ")
                .append(if (eventsModel.repo != null) "in " + eventsModel.repo!!.name!! else "")
    }

    private fun appendReleaseEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val release = eventsModel.payload!!.release
        spannableBuilder.bold("released")
                .append(" ")
                .append(release!!.name)
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendDeleteEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold("deleted")
                .append(" ")
                .append(eventsModel.payload!!.refType)
                .append(" ")
                .append(eventsModel.payload!!.ref)
                .append(" ")
                .bold("at")
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendTeamEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val teamsModel = eventsModel.payload!!.team
        val user = eventsModel.payload!!.user
        spannableBuilder.bold("added")
                .append(" ")
                .append(if (user != null) user.login else eventsModel.repo!!.name)
                .append(" ")
                .bold("in")
                .append(" ")
                .append(if (teamsModel!!.name != null) teamsModel.name else teamsModel.slug)
    }

    private fun appendPushEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        var ref = eventsModel.payload!!.ref
        if (ref!!.startsWith("refs/heads/")) {
            ref = ref.substring(11)
        }
        spannableBuilder.bold("pushed to")
                .append(" ")
                .append(ref)
                .append(" ")
                .bold("at")
                .append(" ")
                .append(if (eventsModel.repo != null) eventsModel.repo!!.name else "null repo")
        val commits = eventsModel.payload!!.commits
        val size = commits?.size ?: -1
        val spanCommits = SpannableBuilder.builder()
        if (size > 0) {
            if (size != 1)
                spanCommits.append(eventsModel.payload!!.size.toString()).append(" new commits").append("\n")
            else
                spanCommits.append("1 new commit").append("\n")
            val max = 5
            var appended = 0
            for (commit in commits!!) {
                if (commit == null) continue
                var sha = commit.sha
                if (TextUtils.isEmpty(sha)) continue
                sha = if (sha!!.length > 7) sha.substring(0, 7) else sha
                spanCommits.url(sha).append(" ")
                        .append(if (commit.message != null) commit.message!!.replace("\\r?\\n|\\r".toRegex(), " ") else "")
                        .append("\n")
                appended++
                if (appended == max) break
            }
        }
        if (spanCommits.length > 0) {
            val last = spanCommits.length
            description!!.maxLines = 5
            description!!.text = spanCommits.delete(last - 1, last)
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.maxLines = 2
            description!!.visibility = View.GONE
        }
    }

    private fun appendPullRequestReviewCommentEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val pullRequest = eventsModel.payload!!.pullRequest
        val comment = eventsModel.payload!!.comment
        spannableBuilder.bold("reviewed")
                .append(" ")
                .bold("pull request")
                .append(" ")
                .bold("in")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .bold("#")
                .bold(pullRequest!!.number.toString())
        if (comment!!.body != null) {
            MarkDownProvider.stripMdText(description!!, comment.body!!.replace("\\r?\\n|\\r".toRegex(), " "))
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.visibility = View.GONE
        }
    }

    private fun appendPullRequestEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val issue = eventsModel.payload!!.pullRequest
        var action = eventsModel.payload!!.action
        if ("synchronize" == action) {
            action = "updated"
        }
        if (eventsModel.payload!!.pullRequest!!.merged) {
            action = "merged"
        }
        spannableBuilder.bold(action)
                .append(" ")
                .bold("pull request")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .bold("#")
                .bold(issue!!.number.toString())
        if ("opened" == action || "closed" == action) {
            if (issue.title != null) {
                MarkDownProvider.stripMdText(description!!, issue.title!!.replace("\\r?\\n|\\r".toRegex(), " "))
                description!!.visibility = View.VISIBLE
            } else {
                description!!.text = ""
                description!!.visibility = View.GONE
            }
        }
    }

    private fun appendPublicEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        var action = "public"
        if (eventsModel.payload != null && "privatized".equals(eventsModel.payload!!.action!!, ignoreCase = true)) {
            action = "private"
        }
        spannableBuilder.append("made")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .append(" ")
                .append(action)
    }

    private fun appendMemberEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val user = eventsModel.payload!!.member
        spannableBuilder.bold("added")
                .append(" ")
                .append(if (user != null) user.login!! + " " else "")
                .append("as a collaborator")
                .append(" ")
                .append("to")
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendIssueEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val issue = eventsModel.payload!!.issue
        val isLabel = "label" == eventsModel.payload!!.action
        val label = if (isLabel)
            if (issue!!.labels != null && !issue.labels!!.isEmpty())
                issue.labels!![issue.labels!!.size - 1]
            else
                null
        else
            null
        spannableBuilder.bold(if (isLabel && label != null) "Labeled " + label.name!! else eventsModel.payload!!.action)
                .append(" ")
                .bold("issue")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .bold("#")
                .bold(issue!!.number.toString())
        if (issue.title != null) {
            MarkDownProvider.stripMdText(description!!, issue.title!!.replace("\\r?\\n|\\r".toRegex(), " "))
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.visibility = View.GONE
        }
    }

    private fun appendIssueCommentEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val comment = eventsModel.payload!!.comment
        val issue = eventsModel.payload!!.issue
        spannableBuilder.bold("commented")
                .append(" ")
                .bold("on")
                .append(" ")
                .bold(if (issue!!.pullRequest != null) "pull request" else "issue")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .bold("#")
                .bold(issue.number.toString())
        if (comment!!.body != null) {
            MarkDownProvider.stripMdText(description!!, comment.body!!.replace("\\r?\\n|\\r".toRegex(), " "))
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.visibility = View.GONE
        }
    }

    private fun appendGollumEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val wiki = eventsModel.payload!!.pages
        if (wiki != null && !wiki.isEmpty()) {
            for ((pageName, _, _, action) in wiki) {
                spannableBuilder.bold(action)
                        .append(" ")
                        .append(pageName)
                        .append(" ")
            }
        } else {
            spannableBuilder.bold(resources.getString(R.string.gollum))
                    .append(" ")
        }
        spannableBuilder
                .append(eventsModel.repo!!.name)

    }

    private fun appendGistEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        var action = eventsModel.payload!!.action
        action = if ("create" == action) "created" else if ("update" == action) "updated" else action
        spannableBuilder.bold(action)
                .append(" ")
                .append(itemView.resources.getString(R.string.gist))
                .append(" ")
                .append(eventsModel.payload!!.gist!!.gistId)
    }

    private fun appendForkEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold("forked")
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendFollowEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold("started following")
                .append(" ")
                .bold(eventsModel.payload!!.target!!.login)
    }

    private fun appendDownloadEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold("uploaded a file")
                .append(" ")
                .append(if (eventsModel.payload!!.download != null) eventsModel.payload!!.download!!.name else "")
                .append(" ")
                .append("to")
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendCreateEvent(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val payloadModel = eventsModel.payload
        val refType = payloadModel!!.refType
        spannableBuilder
                .bold("created")
                .append(" ")
                .append(refType)
                .append(" ")
                .append(if (!"repository".equals(refType!!, ignoreCase = true)) payloadModel.ref!! + " " else "")
                .bold("at")
                .append(" ")
                .append(eventsModel.repo!!.name)
        if (payloadModel.description != null) {
            MarkDownProvider.stripMdText(description!!, payloadModel.description!!.replace("\\r?\\n|\\r".toRegex(), " "))
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.visibility = View.GONE
        }
    }

    private fun appendWatch(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        spannableBuilder.bold(resources.getString(R.string.starred).toLowerCase())
                .append(" ")
                .append(eventsModel.repo!!.name)
    }

    private fun appendCommitComment(spannableBuilder: SpannableBuilder, eventsModel: EventEntity) {
        val comment = if (eventsModel.payload!!.commitComment == null)
            eventsModel.payload!!.comment
        else
            eventsModel.payload!!
                    .commitComment
        val commitId = if (comment != null && comment.commitId != null && comment.commitId!!.length > 10)
            comment.commitId!!.substring(0, 10)
        else
            null
        spannableBuilder.bold("commented")
                .append(" ")
                .bold("on")
                .append(" ")
                .bold("commit")
                .append(" ")
                .append(eventsModel.repo!!.name)
                .url(if (commitId != null) "@$commitId" else "")
        if (comment != null && comment.body != null) {
            MarkDownProvider.stripMdText(description!!, comment.body!!.replace("\\r?\\n|\\r".toRegex(), " "))
            description!!.visibility = View.VISIBLE
        } else {
            description!!.text = ""
            description!!.visibility = View.GONE
        }
    }

    private fun appendActor(eventsModel: EventEntity, spannableBuilder: SpannableBuilder) {
        if (eventsModel.actor != null) {
            spannableBuilder.append(eventsModel.actor!!.login).append(" ")
        }
    }

    private fun appendAvatar(eventsModel: EventEntity) {
        if (avatar != null) {
            //            if (eventsModel.getActor() != null) {
            //                avatar.bindData(avatarLoader, eventsModel.getActor());
            //            } else {
            //                avatar.bindData(null, null);
            //            }
            avatarLoader.loadImage<String>(eventsModel.actor!!.avatarUrl, avatar)
        }
    }

    companion object {
        fun instance(viewGroup: ViewGroup, avatarLoader: AvatarLoader, noImage: Boolean): EventViewHolder {
            return if (noImage) {
                EventViewHolder(viewGroup.inflate(R.layout.item_event_no_image), avatarLoader)
            } else {
                EventViewHolder(viewGroup.inflate(R.layout.item_event), avatarLoader)
            }
        }
    }
}
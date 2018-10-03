package com.duyp.architecture.clean.android.powergit.domain.entities.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by duypham on 10/24/17.
 *
 */
@StringDef({EventType.WatchEvent, EventType.CreateEvent, EventType.CommitCommentEvent, EventType.DownloadEvent, EventType.FollowEvent, EventType.ForkEvent, EventType.GistEvent,
            EventType.GollumEvent, EventType.IssueCommentEvent, EventType.IssuesEvent, EventType.MemberEvent, EventType.PublicEvent, EventType.PullRequestEvent, EventType.PullRequestReviewCommentEvent,
            EventType.PullRequestReviewEvent, EventType.RepositoryEvent, EventType.PushEvent, EventType.TeamAddEvent, EventType.DeleteEvent, EventType.ReleaseEvent,
            EventType.ForkApplyEvent, EventType.OrgBlockEvent, EventType.ProjectCardEvent, EventType.ProjectColumnEvent, EventType.OrganizationEvent, EventType.ProjectEvent})
@Retention(RetentionPolicy.SOURCE)
public @interface EventType {
    String WatchEvent = "WatchEvent";
    String CreateEvent = "CreateEvent";
    String CommitCommentEvent = "CommitCommentEvent";
    String DownloadEvent = "DownloadEvent";
    String FollowEvent = "FollowEvent";
    String ForkEvent = "ForkEvent";
    String GistEvent = "GistEvent";
    String GollumEvent = "GollumEvent";
    String IssueCommentEvent = "IssueCommentEvent";
    String IssuesEvent = "IssuesEvent";
    String MemberEvent = "MemberEvent";
    String PublicEvent = "PublicEvent";
    String PullRequestEvent = "PullRequestEvent";
    String PullRequestReviewCommentEvent = "PullRequestReviewCommentEvent";
    String PullRequestReviewEvent = "PullRequestReviewEvent";
    String RepositoryEvent = "RepositoryEvent";
    String PushEvent = "PushEvent";
    String TeamAddEvent = "TeamAddEvent";
    String DeleteEvent = "DeleteEvent";
    String ReleaseEvent = "ReleaseEvent";
    String ForkApplyEvent = "ForkApplyEvent";
    String OrgBlockEvent = "OrgBlockEvent";
    String ProjectCardEvent = "ProjectCardEvent";
    String ProjectColumnEvent = "ProjectColumnEvent";
    String OrganizationEvent = "OrganizationEvent";
    String ProjectEvent = "ProjectEvent";
}
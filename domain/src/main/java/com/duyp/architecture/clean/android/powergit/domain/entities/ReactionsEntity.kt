package com.duyp.architecture.clean.android.powergit.domain.entities

data class ReactionsEntity(

    var id: Long = 0,

    var url: String? = null,

    var content: String? = null,

    var user: UserEntity? = null,

    var total_count: Int = 0,

        //@SerializedName("+1")
    var plusOne: Int = 0,

        //@SerializedName("-1")
    var minusOne: Int = 0,

    var laugh: Int = 0,

    var hooray: Int = 0,

    var confused: Int = 0,

    var heart: Int = 0,

    var viewerHasReacted: Boolean = false,

    var isCallingApi: Boolean = false
)

    //public static List<ReactionsEntity> getReactionGroup(List<PullRequestTimelineQuery.ReactionGroup> reactions) {
    //    List<ReactionsEntity> models = new ArrayList<>();
    //    if (reactions != null && !reactions.isEmpty()) {
    //        for (PullRequestTimelineQuery.ReactionGroup reaction : reactions) {
    //            ReactionsEntity model = new ReactionsEntity();
    //            model.content = (reaction.content().name());
    //            model.viewerHasReacted = (reaction.viewerHasReacted());
    //            model.total_count = (reaction.users().totalCount());
    //            models.add(model);
    //        }
    //    }
    //    return models;
    //}
    //
    //public static List<ReactionsEntity> getReaction(List<PullRequestTimelineQuery.ReactionGroup1> reactions) {
    //    List<ReactionsEntity> models = new ArrayList<>();
    //    if (reactions != null && !reactions.isEmpty()) {
    //        for (PullRequestTimelineQuery.ReactionGroup1 reaction : reactions) {
    //            ReactionsEntity model = new ReactionsEntity();
    //            model.content = (reaction.content().name());
    //            model.viewerHasReacted = (reaction.viewerHasReacted());
    //            model.total_count = (reaction.users().totalCount());
    //            models.add(model);
    //        }
    //    }
    //    return models;
    //}
    //
    //public static List<ReactionsEntity> getReaction2(List<PullRequestTimelineQuery.ReactionGroup2> reactions) {
    //    List<ReactionsEntity> models = new ArrayList<>();
    //    if (reactions != null && !reactions.isEmpty()) {
    //        for (PullRequestTimelineQuery.ReactionGroup2 reaction : reactions) {
    //            ReactionsEntity model = new ReactionsEntity();
    //            model.content = (reaction.content().name());
    //            model.viewerHasReacted = (reaction.viewerHasReacted());
    //            model.total_count = (reaction.users().totalCount());
    //            models.add(model);
    //        }
    //    }
    //    return models;
    //}
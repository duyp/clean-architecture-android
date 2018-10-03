package com.duyp.architecture.clean.android.powergit.domain.entities.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by duypham on 10/30/17.
 *
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({IssueState.OPEN, IssueState.CLOSED, IssueState.ALL})
public @interface IssueState {
    String OPEN = "open";
    String CLOSED = "closed";
    String ALL = "all";
}

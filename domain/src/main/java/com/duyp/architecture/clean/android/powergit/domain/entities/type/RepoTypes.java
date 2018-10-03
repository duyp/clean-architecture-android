package com.duyp.architecture.clean.android.powergit.domain.entities.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by duypham on 9/16/17.
 * Definitions for github repositories types
 * https://developer.github.com/v3/repos/#list-user-repositories
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({RepoTypes.ALL, RepoTypes.OWNER, RepoTypes.MEMBER})
public @interface RepoTypes {
    String ALL = "all";
    String OWNER = "owner";
    String MEMBER = "member";
}
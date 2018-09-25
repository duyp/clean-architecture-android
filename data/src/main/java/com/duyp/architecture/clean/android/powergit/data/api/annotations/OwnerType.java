package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import androidx.annotation.IntDef;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

import static com.duyp.architecture.clean.android.powergit.data.api.annotations.OwnerType.USER_NORMAL;

@Retention(AnnotationRetention.SOURCE)
@IntDef({USER_NORMAL})
public @interface OwnerType {

    /**
     * Normal user who login with username and password
     */
    int USER_NORMAL = 0;
}
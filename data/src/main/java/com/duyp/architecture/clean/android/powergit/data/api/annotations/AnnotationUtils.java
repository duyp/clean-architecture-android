package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import java.lang.annotation.Annotation;

class AnnotationUtils {

    /**
     * checks if an [Authenticated] annotation exists on this request.
     *
     * @param annotations annotations to check
     *
     * @return if the [Authenticated] annotation exists it returns it, otherwise `null`
     */
    static Authenticated isAuthenticated(final Annotation[] annotations) {
        if(annotations == null)
            return null;

        for(final Annotation annotation : annotations) {
            if(annotation.annotationType().equals(Authenticated.class))
                return (Authenticated) annotation;
        }

        return null;
    }

    static boolean isNoCache(final Annotation[] annotations) {
        if(annotations == null)
            return false;

        for(final Annotation annotation : annotations) {
            if(annotation.annotationType().equals(NoCache.class))
                return true;
        }

        return false;
    }

    static boolean isForceCache(final Annotation[] annotations) {
        if(annotations == null)
            return false;

        for(final Annotation annotation : annotations) {
            if(annotation.annotationType().equals(ForceCache.class))
                return true;
        }

        return false;
    }

    static boolean isPlainRequest(final Annotation[] annotations) {
        if(annotations == null)
            return false;

        for(final Annotation annotation : annotations) {
            if(annotation.annotationType().equals(PlainRequest.class))
                return true;
        }

        return false;
    }
}

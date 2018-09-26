package com.duyp.architecture.clean.android.powergit.data.api.annotations;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import okhttp3.Request;

/**
 * Class holding annotation configurations for retrofit requests
 */
public class RequestAnnotations {

    private final static int KEY_AUTH = 0;
    private final static int KEY_NO_CACHE = 1;
    private final static int KEY_PLAIN_REQUEST = 2;

    private final SparseArray<SparseArray<Object>> mMap = new SparseArray<>();

    // =================================================================================================================
    // AUTHENTICATION
    // =================================================================================================================

    /**
     * Register authentication owner type for given request, see {@link Authenticated}
     * @param request request to be registered
     * @param ownerType type of request owner {@link OwnerType}
     */
    void registerAuthentication(final Request request, final int ownerType) {
        getRequestValues(request, true).put(KEY_AUTH, ownerType);
    }

    /**
     * Get authentication owner type of given request, return null if the request hasn't been register yet
     * @param request request
     * @return null if not found
     */
    @Nullable public @OwnerType Integer getAuthenticationOwnerType(final Request request) {
        return (Integer) getRequestValues(request, false).get(KEY_AUTH);
    }

    // =================================================================================================================
    // NO CACHE
    // =================================================================================================================

    /**
     * Mark a given request as no cache, see {@link NoCache}
     * @param request request
     */
    void registerNoCache(final Request request) {
        getRequestValues(request, true).put(KEY_NO_CACHE, Boolean.TRUE);
    }

    /**
     * Check if the given request was marked as no cache
     * @param request request to be checked
     * @return true if is no cache request, otherwise fail
     */
    public boolean isNoCache(final Request request) {
        return getRequestValues(request, false).get(KEY_NO_CACHE) != null;
    }

    // =================================================================================================================
    // PLAIN REQUEST
    // =================================================================================================================

    /**
     * Mark a given request as plain request, see {@link PlainRequest}
     * @param request request
     */
    void registerPlainRequest(final Request request) {
        getRequestValues(request, true).put(KEY_PLAIN_REQUEST, Boolean.TRUE);
    }

    /**
     * Check if given request was registered as plain request
     * @param request request to be checked
     * @return true if is plain request, otherwise fail
     */
    public boolean isPlainRequest(final Request request) {
        return getRequestValues(request, false).get(KEY_PLAIN_REQUEST) != null;
    }

    /**
     * Get values for the given request including authentication, no cache and plain request values
     * @param request request
     * @param putIfEmpty true if should put an empty map if no value had been added yet
     * @return a sparse array with key is one of {@link #KEY_AUTH}, {@link #KEY_NO_CACHE}, {@link #KEY_PLAIN_REQUEST}
     */
    @NonNull private SparseArray<Object> getRequestValues(final Request request, final boolean putIfEmpty) {
        final int requestId = createUniqueIdentifier(request);
        SparseArray<Object> values = mMap.get(requestId);
        if (values == null) {
            values = new SparseArray<>();
            if (putIfEmpty) mMap.put(requestId, values);
        }
        return values;
    }

    private Integer createUniqueIdentifier(final Request request) {
        return (request.url().toString() + request.method()).hashCode();
    }
}

package com.duyp.architecture.clean.android.powergit.data.api.interceptors

import android.net.Uri
import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

class PaginationInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val headers = chain.request().headers()
        if (headers != null) {
            if (headers.values("Accept").contains("application/vnd.github.html") || headers.values("Accept").contains("application/vnd.github.VERSION.raw")) {
                return response//return them as they are.
            }
        }
        if (response.isSuccessful) {
            if (response.peekBody(1).string() == "[") {
                val json = StringBuilder("{")
                val link = response.header("link")
                if (link != null) {
                    val links = link.split(",".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    for (link1 in links) {
                        val pageLink = link1.split(";".toRegex())
                                .dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        val page = Uri.parse(pageLink[0].replace("[<>]".toRegex(), ""))
                                .getQueryParameter("page")
                        val rel = pageLink[1]
                                .replace("\"".toRegex(), "")
                                .replace("rel=", "")
                        if (page != null)
                            json.append(String.format("\"%s\":\"%s\",", rel.trim { it <= ' ' }, page))
                    }
                }
                json.append(String.format("\"items\":%s}", response.body()!!.string()))
                return response.newBuilder()
                        .body(ResponseBody.create(response.body()!!.contentType(), json.toString()))
                        .build()
            } else if (response.header("link") != null) {
                val link = response.header("link")
                val pagination = StringBuilder()
                val links = link!!.split(",".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                for (link1 in links) {
                    val pageLink = link1.split(";".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val page = Uri.parse(pageLink[0].replace("[<>]".toRegex(), ""))
                            .getQueryParameter("page")
                    val rel = pageLink[1]
                            .replace("\"".toRegex(), "")
                            .replace("rel=", "")
                    if (page != null)
                        pagination.append(String.format("\"%s\":\"%s\",", rel.trim { it <= ' ' }, page))
                }
                if (!TextUtils.isEmpty(pagination.toString())) {//hacking for search pagination.
                    val body = response.body()!!.string()
                    return response.newBuilder()
                            .body(ResponseBody.create(response.body()!!.contentType(), "{" + pagination + body.substring(1, body.length)))
                            .build()
                }
            }
        }
        return response
    }

}

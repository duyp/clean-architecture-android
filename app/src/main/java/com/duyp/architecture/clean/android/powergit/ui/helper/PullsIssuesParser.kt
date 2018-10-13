package com.duyp.architecture.clean.android.powergit.ui.helper

import android.net.Uri

/**
 * Created by Kosh on 17 Dec 2016, 12:17 AM
 */

class PullsIssuesParser {

    var login: String? = null
    var repoId: String? = null
    var number: Int = 0

    override fun toString(): String {
        return "PullsIssuesParser{" +
                "login='" + login + '\''.toString() +
                ", repoId='" + repoId + '\''.toString() +
                ", number=" + number +
                '}'.toString()
    }

    companion object {

        fun getForPullRequest(url: String): PullsIssuesParser? {
            val uri = Uri.parse(url)
            val segments = uri.pathSegments
            if (segments == null || segments.size < 3) return null
            var owner: String? = null
            var repo: String? = null
            var number: String? = null
            if (segments.size > 3) {
                if ("pull" == segments[2] || "pulls" == segments[2]) {
                    owner = segments[0]
                    repo = segments[1]
                    number = segments[3]
                } else if (("pull" == segments[3] || "pulls" == segments[3]) && segments.size > 4) {
                    owner = segments[1]
                    repo = segments[2]
                    number = segments[4]
                } else {
                    return null
                }
            }
            if (InputHelper.isEmpty(number)) return null
            val issueNumber: Int
            try {
                issueNumber = Integer.parseInt(number)
            } catch (nfe: NumberFormatException) {
                return null
            }

            if (issueNumber < 1) return null
            val model = PullsIssuesParser()
            model.login = owner
            model.repoId = repo
            model.number = issueNumber
            return model
        }

        fun getForIssue(url: String): PullsIssuesParser? {
            val uri = Uri.parse(url)
            val segments = uri.pathSegments
            if (segments == null || segments.size < 3) return null
            var owner: String? = null
            var repo: String? = null
            var number: String? = null
            if (segments.size > 3) {
                if (segments[2].equals("issues", ignoreCase = true)) {
                    owner = segments[0]
                    repo = segments[1]
                    number = segments[3]
                } else if (segments[3].equals("issues", ignoreCase = true) && segments.size > 4) {
                    owner = segments[1]
                    repo = segments[2]
                    number = segments[4]
                } else {
                    return null
                }
            }
            if (InputHelper.isEmpty(number))
                return null
            val issueNumber: Int
            try {
                issueNumber = Integer.parseInt(number)
            } catch (nfe: NumberFormatException) {
                return null
            }

            if (issueNumber < 1) return null
            val model = PullsIssuesParser()
            model.login = owner
            model.repoId = repo
            model.number = issueNumber
            return model
        }
    }
}

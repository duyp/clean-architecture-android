package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.utils.CommonUtil


/**
 * Serves a model for the filter in Repositories fragment
 */
class FilterOptions{

    private var type: String? = null

    private var sort = "Pushed"

    private var sortDirection = "descending"

    private var isPersonalProfile: Boolean = false

    private val typesListForPersonalProfile = listOf("Select", "All", "Owner", "Public", "Private", "Member")

    private val typesListForExternalProfile = listOf("Select", "All", "Owner", "Member")

    private val typesListForOrganizationProfile = listOf("Select", "All", "Public", "Private", "Forks", "Sources", "Member")

    val sortOptionList = listOf("Pushed", "Created", "Updated", "Full Name")

    val sortDirectionList = listOf("Descending", "Ascending")

    var isOrg: Boolean = false

    val selectedTypeIndex: Int
        get() = if (isPersonalProfile) {
            typesListForPersonalProfile.indexOf(type)
        } else {
            typesListForExternalProfile.indexOf(type)
        }

    val selectedSortOptionIndex: Int
        get() = sortOptionList.indexOf(sort)

    val selectedSortDirectionIndex: Int
        get() = sortDirectionList.indexOf(sortDirection)

    val typesList: List<String>
        get() = if (isPersonalProfile) {
            typesListForPersonalProfile
        } else if (isOrg) {
            typesListForOrganizationProfile
        } else {
            typesListForExternalProfile
        }

    fun setType(type: String) {
        this.type = type
    }

    fun setSort(sort: String) {
        this.sort = sort
    }

    fun setSortDirection(sortDirection: String) {
        this.sortDirection = sortDirection
    }

    fun getQueryMap(): Map<String, String> {
        val queryMap: MutableMap<String, String> = HashMap()
        if (CommonUtil.isEmpty(type) || "Select".equals(type!!, ignoreCase = true)) {
            queryMap.remove(TYPE)
            queryMap[AFFILIATION] = "owner,collaborator"
        } else {
            queryMap.remove(AFFILIATION)
            queryMap[TYPE] = type!!.toLowerCase()
        }
        //Not supported for organization repo
        if (!isOrg) {
            if (sort.contains(" ")) {
                //full name should be full_name
                queryMap[SORT] = sort.replace(" ", "_").toLowerCase()
            } else {
                queryMap[SORT] = sort.toLowerCase()
            }
            if (sortDirection == sortDirectionList.get(0)) {
                //Descending should be desc
                queryMap[DIRECTION] = sortDirection.toLowerCase().substring(0, 4)
            } else {
                //Ascending should be asc
                queryMap[DIRECTION] = sortDirection.toLowerCase().substring(0, 3)
            }
        }
        return queryMap
    }

    fun setIsPersonalProfile(isPersonalProfile: Boolean) {
        this.isPersonalProfile = isPersonalProfile
    }

    companion object {

        private val TYPE = "type"
        private val SORT = "sort"
        private val AFFILIATION = "affiliation"
        private val DIRECTION = "direction"
    }
}

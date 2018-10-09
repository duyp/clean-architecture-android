package com.duyp.architecture.clean.android.powergit.domain.entities

import com.duyp.architecture.clean.android.powergit.domain.assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ListEntityTest {

    @Test fun canLoadMore() {
        val list = ListEntity<Any>(next = 2, last = 3)
        assertTrue(list.canLoadMore())

        val list1 = ListEntity<Any>(next = 0, last = 3)
        assertFalse(list1.canLoadMore())

        val list2 = ListEntity<Any>(first = 0, next = 0, last = 3)
        assertTrue(list2.canLoadMore())

        val list3 = ListEntity<Any>(last = 2, next = null)
        assertFalse(list3.canLoadMore())

        val list4 = ListEntity<Any>(last = null, next = 2)
        assertFalse(list4.canLoadMore())
    }

    @Test fun mergeList_firstPageShouldNotMergeAnyway() {
        val list = ListEntity(first = 1, next = 2, last = 5, items = listOf(1, 2, 3, 4))
        val previousList = ListEntity(next = 3, last = 4, items = listOf(10, 11, 12, 13, 14))

        list.mergeWith(previousList).assert {
            getNextPage() == 2 && getLastPage() == 5 && items.size == 4 && getFirstPage() == 1
        }
    }

    @Test fun mergeList_previousPageEmptyShouldNotMergeAnyway() {
        val list = ListEntity(first = 1, next = 3, last = 5, items = listOf(1, 2, 3, 4))
        val previousList = ListEntity<Int>(next = 2, last = 5, items = emptyList())

        list.mergeWith(previousList).assert {
            getNextPage() == 3 && getLastPage() == 5 && items.size == 4
        }
    }

    @Test fun mergeList_isLastPage_shouldMergeAnyway() {
        val list = ListEntity(first = 1, next = null, prev = 2, last = null, items = listOf(8,  9, 10))
        val previousList = ListEntity(next = 2, last = 3, items = listOf(5, 6, 7))

        list.mergeWith(previousList).assert { items.size == 6 }
    }

    @Test fun mergeList_normalSituation() {
        // page 3
        val list = ListEntity(next = 4, prev = 2, last = 10, items = listOf(8,  9, 10, 11, 12))
        // page 2
        val previousList = ListEntity(next = 3, prev = 1, last = 10, items = listOf(5, 6, 7))

        list.mergeWith(previousList).assert { items.size == 8 }
    }

    @Test fun mergeList_pageInforNotValid_shouldNotMerge() {
        val list = ListEntity(next = 4, prev = 2, last = 10, items = listOf(8,  9, 10, 11, 12))
        val previousList = ListEntity(next = 5, prev = 1, last = 10, items = listOf(5, 6, 7))

        list.mergeWith(previousList).assert { items.size == 5 }
    }
}
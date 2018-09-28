package com.duyp.architecture.clean.android.powergit.data

import org.junit.Test
import java.util.*

class MapperGenerator {

    @Test
    fun generateMappers() {
//        Assert.assertEquals("abc", listObjectFields(UserLocalData::class.java, "\n",
//                "localData.%s = e.%s"))
    }

    fun listObjectFields(classA: Class<*>, separator: String, format: String): String {
        val sb = StringBuilder()
        val fields = classA.declaredFields
        for (f in fields) {
            val name = f.name
            sb.append(String.format(Locale.ENGLISH, format, name, name)).append(separator)
        }
        return sb.toString()
    }
}
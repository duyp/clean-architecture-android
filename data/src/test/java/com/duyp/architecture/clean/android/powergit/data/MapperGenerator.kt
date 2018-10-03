package com.duyp.architecture.clean.android.powergit.data

import com.duyp.architecture.clean.android.powergit.domain.entities.EventEntity
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Use this test to generate mapper codes,
 * eg [com.duyp.architecture.clean.android.powergit.data.entities.user.UserApiToLocalMapper]
 */
class MapperGenerator {

    @Test
    fun generateMappers() {

        Assert.assertEquals("abc", listObjectFields(EventEntity::class.java, "\n", "entity", "e"))
    }

    fun listObjectFields(classA: Class<*>, separator: String, aName: String, bName: String): String {
        val sb = StringBuilder()
        sb.append("val $aName = ${classA.simpleName}()").append(separator)
        val format = "$aName.%s = $bName.%s"
        val fields = classA.declaredFields
        for (f in fields) {
            val name = f.name
            sb.append(String.format(Locale.ENGLISH, format, name, name)).append(separator)
        }
        sb.append("return $aName").append(separator)
        return sb.toString()
    }
}
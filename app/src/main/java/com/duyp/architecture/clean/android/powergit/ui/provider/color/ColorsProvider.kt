package com.duyp.architecture.clean.android.powergit.ui.provider.color

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.text.TextUtils
import com.duyp.architecture.clean.android.powergit.domain.entities.LanguageColorEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import io.reactivex.Completable
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * Created by Kosh on 27 May 2017, 9:50 PM
 *
 * Modified by Duy Pham (10/2018)
 */

object ColorsProvider {

    //predefined languages.
    private val POPULAR_LANG = listOf("Java", "Kotlin", "JavaScript", "Python", "CSS", "PHP",
            "Ruby", "C++", "C", "GO", "Swift")

    private val colors = LinkedHashMap<String, LanguageColorEntity>()

    fun load(context: Context) {
        if (colors.isEmpty()) {
            Completable.fromAction {
                try {
                    val type = object : TypeToken<Map<String, LanguageColorEntity>>() {}.type
                    context.assets.open("colors.json").use { stream ->
                        val gson = Gson()
                        JsonReader(InputStreamReader(stream)).use { reader ->
                            colors.putAll(gson.fromJson<Map<out String, LanguageColorEntity>>(reader, type))
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.onErrorComplete().subscribe()
        }
    }

    fun languages(): ArrayList<String> {
        val lang = ArrayList<String>()
        lang.addAll(colors
                .filter { !TextUtils.isEmpty(it.key) }
                .map { it.key }
        )
        lang.add(0, "All Languages")
        lang.addAll(1, POPULAR_LANG)
        return lang
    }

    fun getColor(lang: String): LanguageColorEntity? {
        return colors[lang]
    }

    @ColorInt
    fun getColorAsColor(lang: String, context: Context): Int {
        val color = getColor(lang)
        var langColor = ColorGenerator.getColor(context, lang)
        if (color != null && !TextUtils.isEmpty(color.color)) {
            try {
                langColor = Color.parseColor(color.color)
            } catch (ignored: Exception) {
            }

        }
        return langColor
    }
}

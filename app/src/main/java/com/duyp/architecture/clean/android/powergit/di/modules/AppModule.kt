package com.duyp.architecture.clean.android.powergit.di.modules

import android.accounts.AccountManager
import android.content.Context
import com.duyp.architecture.clean.android.powergit.PowerGitApp
import com.duyp.architecture.clean.android.powergit.R
import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants
import com.duyp.architecture.clean.android.powergit.data.di.AccountType
import com.google.gson.*
import dagger.Module
import dagger.Provides
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(powerGitApp: PowerGitApp) = powerGitApp.applicationContext

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(Date::class.java, DateDeserializer())
                .create()
    }

    @Provides
    @Singleton
    fun provideAccountManager(context: Context) = AccountManager.get(context)

    @Provides
    @Singleton
    fun provideSharedPreference(context: Context) = context.getSharedPreferences("power-git-preference", 0)

    @Provides
    @Singleton
    @AccountType
    fun provideAccountType(context: Context) = context.getString(R.string.account_type)
}

private class DateDeserializer : JsonDeserializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
        val date = element.asString
        val formatter = SimpleDateFormat(ApiConstants.DATE_TIME_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        try {
            return formatter.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }
}
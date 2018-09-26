package com.duyp.architecture.clean.android.powergit.data.di

import android.content.Context
import com.duyp.architecture.clean.android.powergit.data.BuildConfig
import com.duyp.architecture.clean.android.powergit.data.api.ApiConstants.TIME_OUT_API
import com.duyp.architecture.clean.android.powergit.data.api.UserService
import com.duyp.architecture.clean.android.powergit.data.api.annotations.OwnerType
import com.duyp.architecture.clean.android.powergit.data.api.annotations.RequestAnnotations
import com.duyp.architecture.clean.android.powergit.data.api.converters.GithubResponseConverter
import com.duyp.architecture.clean.android.powergit.data.api.interceptors.AuthorizationInterceptor
import com.duyp.architecture.clean.android.powergit.data.api.interceptors.CacheInterceptor
import com.duyp.architecture.clean.android.powergit.data.api.interceptors.ContentTypeInterceptor
import com.duyp.architecture.clean.android.powergit.data.api.interceptors.PaginationInterceptor
import com.duyp.architecture.clean.android.powergit.domain.usecases.GetAuthentication
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule() {

    @Provides
    @Singleton
    fun provideRequestAnnotations() = RequestAnnotations()

    @Provides
    @Singleton
    fun provideOkHttpClient(context: Context, requestAnnotations: RequestAnnotations,
                            getAuthentication: GetAuthentication): OkHttpClient {
        // okHttp client
        val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(TIME_OUT_API.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_API.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_API.toLong(), TimeUnit.SECONDS)
                .addInterceptor(AuthorizationInterceptor(requestAnnotations) { ownerType ->
                    var token: String? = null
                    when (ownerType) {
                        OwnerType.USER_NORMAL -> token = getAuthentication.getCurrentUserAuthentication()
                                .subscribeOn(Schedulers.io())
                                .blockingGet()
                    }
                    token
                })
                .addInterceptor(ContentTypeInterceptor())
                .addInterceptor(PaginationInterceptor())
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)

        // cache
        var cache: Cache? = null
        try {
            val httpCacheDirectory = File(context.cacheDir, "httpCache")
            cache = Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong()) // 10 MB
        } catch (e: Exception) {
        }

        if (cache != null) {
            clientBuilder
                    .cache(cache)
                    .addInterceptor(CacheInterceptor(requestAnnotations))
        }

        // add logging interceptor
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addNetworkInterceptor(logging)
        }

        return clientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) = Retrofit.Builder()
            .baseUrl(BuildConfig.REST_URL)
            .client(okHttpClient)
            .addConverterFactory(GithubResponseConverter(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)
}
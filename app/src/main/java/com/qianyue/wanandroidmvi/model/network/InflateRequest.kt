package com.qianyue.wanandroidmvi.model.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.qianyue.wanandroidmvi.WanApplication
import com.qianyue.wanandroidmvi.constant.LOG
import com.qianyue.wanandroidmvi.utils.WanLog
import okhttp3.Cache
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author QianYue
 * @since 2023/8/16
 */

val API_SERVICE: ApiService by lazy {
    Retrofit.Builder()
        .baseUrl(ApiService.BASE_URL)
        .client(initClient())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(ApiService::class.java)
}

private fun initClient(): OkHttpClient = OkHttpClient.Builder()
    .cache(Cache(File(WanApplication.instance.cacheDir, "wan_wan_cache"), 500 shl 20))
    .cookieJar(cookieJar)
    .connectTimeout(ApiService.CONNECT_TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(ApiService.READ_TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(ApiService.WRITE_TIMEOUT, TimeUnit.SECONDS)
    .addInterceptor(logInterceptor)
    .build()


val cookieJar: CookieJar by lazy {
    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(WanApplication.instance))
}

val logInterceptor : Interceptor by lazy {
    Interceptor { chain ->
        val request = chain.request()
        WanLog.d(msg = request.url().toString())
        val response = chain.proceed(request)
//        WanLog.d(msg = response.body()?.string())
        response
    }
}
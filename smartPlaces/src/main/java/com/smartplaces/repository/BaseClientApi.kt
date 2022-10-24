package com.smartplaces.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by mohamed abd elnaby on 16/April/2019
 */

internal object BaseClientApi {
    private const val BASIC_URL = "https://maps.googleapis.com/"

    fun getRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASIC_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    private val gson: Gson
        get() = GsonBuilder()
            .setLenient()
            .create()

    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }


    private fun buildOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        okHttpClientBuilder.readTimeout(3, TimeUnit.MINUTES)
        okHttpClientBuilder.writeTimeout(5, TimeUnit.MINUTES)
        return okHttpClientBuilder.build()
    }


}

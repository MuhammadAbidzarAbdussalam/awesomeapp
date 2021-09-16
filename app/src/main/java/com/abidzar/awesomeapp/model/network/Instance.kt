package com.abidzar.awesomeapp.model.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val baseURL = "https://api.pexels.com/v1/"
const val apiKey = "563492ad6f917000010000019b86a49fe4f24b38942b798bb91f1e34"
const val posterBaseURL = "https://image.tmdb.org/t/p/original/"

const val firstPage = 1
const val postPerPage = 20

class Instance {

    companion object {

        fun getInstance() : Service {

            val requestInterceptor = Interceptor {chain ->


                val url : HttpUrl = chain.request()
                    .url
                    .newBuilder()
                    .build()

                val request: Request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient: OkHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(requestInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)
        }
    }

}
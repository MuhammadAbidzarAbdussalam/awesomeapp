package com.abidzar.awesomeapp.model.network

import com.abidzar.awesomeapp.model.data.curatedphotos.CuratedPhotos
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {

    @GET("curated")
    fun getCuratedPhotos(@Query("page") page: Int, @Query("per_page") per_page: Int): Single<CuratedPhotos>

}
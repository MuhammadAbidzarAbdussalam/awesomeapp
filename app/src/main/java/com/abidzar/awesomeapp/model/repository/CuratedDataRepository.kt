package com.abidzar.awesomeapp.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.abidzar.awesomeapp.model.network.Service
import com.abidzar.awesomeapp.model.network.postPerPage
import com.abidzar.awesomeapp.model.source.CuratedDataSource
import com.abidzar.awesomeapp.model.source.CuratedDataSourceFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CuratedDataRepository (private val apiService: Service) {

    lateinit var photoPagedList: LiveData<PagedList<Photo>>
    lateinit var curatedDataSourceFactory: CuratedDataSourceFactory

    fun fetchCuratedPhotoPagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Photo>> {
        curatedDataSourceFactory = CuratedDataSourceFactory(apiService, compositeDisposable)

        val config:PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(postPerPage)
            .build()

        photoPagedList = LivePagedListBuilder(curatedDataSourceFactory, config).build()

        return photoPagedList
    }

    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<CuratedDataSource, NetworkState>(curatedDataSourceFactory.curatedPhotoLiveDataSource, CuratedDataSource::networkState)
    }

}
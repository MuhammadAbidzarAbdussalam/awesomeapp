package com.abidzar.awesomeapp.model.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.abidzar.awesomeapp.model.network.Service
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CuratedDataSourceFactory (
    private val apiService: Service,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Photo>() {

    val curatedPhotoLiveDataSource = MutableLiveData<CuratedDataSource>()

    override fun create(): DataSource<Int, Photo> {
        val curatedDataSource = CuratedDataSource(apiService, compositeDisposable)

        curatedPhotoLiveDataSource.postValue(curatedDataSource)
        return curatedDataSource
    }
}
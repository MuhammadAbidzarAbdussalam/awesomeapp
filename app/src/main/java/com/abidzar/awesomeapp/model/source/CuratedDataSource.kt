package com.abidzar.awesomeapp.model.source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.abidzar.awesomeapp.model.network.Service
import com.abidzar.awesomeapp.model.network.apiKey
import com.abidzar.awesomeapp.model.network.firstPage
import com.abidzar.awesomeapp.model.network.postPerPage
import com.abidzar.awesomeapp.model.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CuratedDataSource(
    private val apiService: Service,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Photo>() {

    private var page = firstPage

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getCuratedPhotos(apiKey, params.key, postPerPage)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.total_results / postPerPage >= params.key) {
                        callback.onResult(it.photos, params.key + 1)
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.ENDOFLIST)
                    }
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("PopularDataSource", it.message.toString())
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getCuratedPhotos(apiKey, page, postPerPage)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.photos, null, page + 1)
                    networkState.postValue(NetworkState.LOADED)
                }, {
                    networkState.postValue(NetworkState.ERROR)
                    Log.e("PopularDataSource", it.message.toString())
                })
        )
    }
}
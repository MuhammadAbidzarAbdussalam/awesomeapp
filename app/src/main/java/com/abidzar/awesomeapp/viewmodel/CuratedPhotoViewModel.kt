package com.abidzar.awesomeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.abidzar.awesomeapp.model.repository.CuratedDataRepository
import com.abidzar.awesomeapp.model.repository.NetworkState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CuratedPhotoViewModel (private val curatedDataRepository: CuratedDataRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val curatedPhotoPagedList : LiveData<PagedList<Photo>> by lazy {
        curatedDataRepository.fetchCuratedPhotoPagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        curatedDataRepository.getNetworkState()
    }

    fun listIsEmpty():Boolean {
        return curatedPhotoPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}
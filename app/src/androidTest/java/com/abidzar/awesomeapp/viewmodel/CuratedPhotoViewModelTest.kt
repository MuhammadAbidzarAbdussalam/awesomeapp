package com.abidzar.awesomeapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abidzar.awesomeapp.getOrAwaitValue
import com.abidzar.awesomeapp.model.network.Instance
import com.abidzar.awesomeapp.model.network.Service
import com.abidzar.awesomeapp.model.repository.CuratedDataRepository
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CuratedPhotoViewModelTest {

    private lateinit var curatedPhotoViewModel: CuratedPhotoViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        val apiService: Service = Instance.getInstance()
        val curatedDataRepository = CuratedDataRepository(apiService)
        curatedPhotoViewModel = CuratedPhotoViewModel(curatedDataRepository)

    }

    @Test
    fun testCuratedPhotoViewModel() {

        val result = curatedPhotoViewModel.curatedPhotoPagedList.getOrAwaitValue()

        Log.d("ISINYA", result.toString())

        assertThat(result).isNotNull()

    }

}
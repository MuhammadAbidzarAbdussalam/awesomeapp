package com.abidzar.awesomeapp.view

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.abidzar.awesomeapp.R
import com.abidzar.awesomeapp.databinding.ActivityScrollingBinding
import com.abidzar.awesomeapp.model.network.Instance
import com.abidzar.awesomeapp.model.network.Service
import com.abidzar.awesomeapp.model.repository.CuratedDataRepository
import com.abidzar.awesomeapp.model.repository.NetworkState
import com.abidzar.awesomeapp.view.adapter.CuratedPhotosPagedListAdapter
import com.abidzar.awesomeapp.viewmodel.CuratedPhotoViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.progressBar
import kotlinx.android.synthetic.main.content_scrolling.txvError
import kotlinx.android.synthetic.main.grid_item.view.*
import android.view.WindowManager

import android.os.Build


class ScrollingActivity : AppCompatActivity() {

    private lateinit var curatedPhotoViewModel: CuratedPhotoViewModel

    lateinit var curatedDataRepository: CuratedDataRepository

    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title

        val apiService: Service = Instance.getInstance()

        curatedDataRepository = CuratedDataRepository(apiService)

        viewModelFactory = CuratedPhotoViewModelFactory(curatedDataRepository)
        curatedPhotoViewModel = getViewModel()

        val gridLayoutManager = GridLayoutManager(this, 2)
        val curatedPhotosPagedListAdapter = CuratedPhotosPagedListAdapter(this, gridLayoutManager)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = curatedPhotosPagedListAdapter.getItemViewType(position)

                if (viewType == curatedPhotosPagedListAdapter.PHOTO_LIST_VIEW_TYPE || viewType == curatedPhotosPagedListAdapter.PHOTO_GRID_VIEW_TYPE) return 1
                else return 3
            }
        }

        rvCuratedPhotos.layoutManager = gridLayoutManager
        rvCuratedPhotos.setHasFixedSize(true)
        rvCuratedPhotos.adapter = curatedPhotosPagedListAdapter

        curatedPhotoViewModel.curatedPhotoPagedList.observe(this, Observer {
            curatedPhotosPagedListAdapter.submitList(it)

            if (it.size > 0) {
                val photo = it.random()
                val moviePosterURL: String = photo.src.original
                Glide.with(this)
                    .load(moviePosterURL)
                    .into(imvBanner)

                val window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.parseColor(photo.avg_color)
            }

        })

        curatedPhotoViewModel.networkState.observe(this, Observer {
            progressBar.visibility =
                if (curatedPhotoViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txvError.visibility =
                if (curatedPhotoViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if (!curatedPhotoViewModel.listIsEmpty()) {
                curatedPhotosPagedListAdapter.setNetworkState(it)
            }
        })

        val density: Float = resources
            .displayMetrics.density

        imvGridType.setOnClickListener {

            imvGridType.layoutParams.height = pxToDp(18, density)
            imvGridType.layoutParams.width = pxToDp(18, density)
            imvGridType.requestLayout()

            imvListType.layoutParams.height = pxToDp(14, density)
            imvListType.layoutParams.width = pxToDp(14, density)
            imvListType.requestLayout()

            gridLayoutManager.spanCount = 2

            curatedPhotosPagedListAdapter.notifyItemChanged(
                0,
                curatedPhotosPagedListAdapter.itemCount
            )

        }

        imvListType.setOnClickListener {

            imvListType.layoutParams.height = pxToDp(18, density)
            imvListType.layoutParams.width = pxToDp(18, density)
            imvListType.requestLayout()

            imvGridType.layoutParams.height = pxToDp(14, density)
            imvGridType.layoutParams.width = pxToDp(14, density)
            imvGridType.requestLayout()

            gridLayoutManager.spanCount = 1

            curatedPhotosPagedListAdapter.notifyItemChanged(
                0,
                curatedPhotosPagedListAdapter.itemCount
            )

        }

    }

    fun pxToDp(dp: Int, density: Float): Int {
        return Math.round(dp.toFloat() * density)
    }

    private fun getViewModel(): CuratedPhotoViewModel {
        return ViewModelProvider(this, viewModelFactory).get(CuratedPhotoViewModel::class.java)
    }

    class CuratedPhotoViewModelFactory(private var moviePopularRepository: CuratedDataRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return CuratedPhotoViewModel(moviePopularRepository) as T
        }
    }

}
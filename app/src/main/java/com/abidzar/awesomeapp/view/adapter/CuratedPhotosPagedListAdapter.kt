package com.abidzar.awesomeapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abidzar.awesomeapp.R
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.abidzar.awesomeapp.model.repository.NetworkState
import com.abidzar.awesomeapp.view.DetailPhotoActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.grid_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class CuratedPhotosPagedListAdapter(public val context: Context, public val layoutManager: GridLayoutManager) :
    PagedListAdapter<Photo, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val SPAN_COUNT_ONE = 1
    val SPAN_COUNT_TWO = 2

    val PHOTO_LIST_VIEW_TYPE = 1
    val PHOTO_GRID_VIEW_TYPE = 2
    val NETWORK_VIEW_TYPE = 3

    private var networkState: NetworkState? = null

    class MovieDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == PHOTO_LIST_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.list_item, parent, false)
            return PhotoItemViewHolder(view)
        } else if (viewType == PHOTO_GRID_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.grid_item, parent, false)
            return PhotoItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == PHOTO_LIST_VIEW_TYPE || getItemViewType(position) == PHOTO_GRID_VIEW_TYPE) {
            (holder as PhotoItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            val spanCount = layoutManager.spanCount
            if (spanCount == SPAN_COUNT_ONE){
                PHOTO_LIST_VIEW_TYPE
            } else {
                PHOTO_GRID_VIEW_TYPE
            }
        }
    }


    class PhotoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(photo: Photo?, context: Context) {

            itemView.txvPhotoDesc.text = photo?.photographer

            val src = photo?.src

            val photoURL: String = src!!.original
            Glide.with(itemView.context)
                .load(photoURL)
                .into(itemView.imvPhoto)

            itemView.imvPhoto.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, DetailPhotoActivity::class.java)
                intent.putExtra("photo", photo)
                context.startActivity(intent)
            })

        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progressBarItem.visibility = View.VISIBLE
            } else {
                itemView.progressBarItem.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.txvErrorMsgItem.visibility = View.VISIBLE
                itemView.txvErrorMsgItem.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.txvErrorMsgItem.visibility = View.VISIBLE
                itemView.txvErrorMsgItem.text = networkState.msg
            } else {
                itemView.txvErrorMsgItem.visibility = View.GONE
            }

        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

}
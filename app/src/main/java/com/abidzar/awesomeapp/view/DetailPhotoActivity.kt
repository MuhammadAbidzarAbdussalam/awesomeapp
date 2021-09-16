package com.abidzar.awesomeapp.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.abidzar.awesomeapp.R
import com.abidzar.awesomeapp.model.data.curatedphotos.Photo
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_photo.*
import kotlinx.android.synthetic.main.grid_item.view.*
import android.content.Intent
import android.net.Uri


class DetailPhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_photo)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val photo: Photo? = intent.getParcelableExtra("photo")

        val src = photo?.src

        val photoURL: String = src!!.original
        Glide.with(this)
            .load(photoURL)
            .into(imvBannerDetail)

        toolbar_layout.title = photo.photographer

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(photo.avg_color)

        fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(photo.avg_color));

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Opening Source", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()

            openLink(photo.url)

        }

        imvOriginalLink.setOnClickListener {
            openLink(src.original)
        }

        imvLarge2Link.setOnClickListener {
            openLink(src.large2x)
        }

        imvLargeLink.setOnClickListener {
            openLink(src.large)
        }

        imvMediumLink.setOnClickListener {
            openLink(src.medium)
        }

        imvSmallLink.setOnClickListener {
            openLink(src.small)
        }

        imvPortraitLink.setOnClickListener {
            openLink(src.portrait)
        }

        imvLandscapeLink.setOnClickListener {
            openLink(src.landscape)
        }

        imvTinyLink.setOnClickListener {
            openLink(src.tiny)
        }

        Glide.with(this)
            .load(src.original)
            .into(imvOriginal)

        Glide.with(this)
            .load(src.large2x)
            .into(imvLarge2)

        Glide.with(this)
            .load(src.large)
            .into(imvLarge)

        Glide.with(this)
            .load(src.medium)
            .into(imvMedium)

        Glide.with(this)
            .load(src.small)
            .into(imvSmall)

        Glide.with(this)
            .load(src.portrait)
            .into(imvPortrait)

        Glide.with(this)
            .load(src.landscape)
            .into(imvLandscape)

        Glide.with(this)
            .load(src.tiny)
            .into(imvTiny)

    }

    private fun openLink(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
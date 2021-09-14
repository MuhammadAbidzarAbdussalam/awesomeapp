package com.abidzar.awesomeapp.model.data.curatedphotos

data class CuratedPhotos(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)
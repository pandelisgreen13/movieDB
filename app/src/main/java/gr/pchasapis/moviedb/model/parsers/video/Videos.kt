package gr.pchasapis.moviedb.model.parsers.video

import com.google.gson.annotations.SerializedName

data class Videos(@SerializedName("results") val videoResultList: List<VideoResultsItem>? = null)
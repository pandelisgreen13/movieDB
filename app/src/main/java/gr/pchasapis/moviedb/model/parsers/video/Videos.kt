package gr.pchasapis.moviedb.model.parsers.video


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Videos(@SerialName("results") val videoResultList: List<VideoResultsItem>? = null)

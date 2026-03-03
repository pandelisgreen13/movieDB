package gr.pchasapis.moviedb.model.parsers.genre

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenresItem(
    @SerialName("name") val name: String? = null,
    @SerialName("id") val id: Int? = null
)

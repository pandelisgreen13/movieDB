package gr.pchasapis.moviedb.model.parsers.video

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VideoResultsItem(
	@SerialName("site") val site: String? = null,
	@SerialName("size") val size: Int? = null,
	@SerialName("name") val name: String? = null,
	@SerialName("id") val id: String? = null,
	@SerialName("type") val type: String? = null,
	@SerialName("key") val key: String? = null
)

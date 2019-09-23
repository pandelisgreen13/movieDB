package gr.pchasapis.moviedb.model.parsers.video

import com.google.gson.annotations.SerializedName

data class VideoResultsItem(@SerializedName("site") val site: String? = null,
							@SerializedName("size") val size: Int? = null,
							@SerializedName("name") val name: String? = null,
							@SerializedName("id") val id: String? = null,
							@SerializedName("type") val type: String? = null,
							@SerializedName("key") val key: String? = null)
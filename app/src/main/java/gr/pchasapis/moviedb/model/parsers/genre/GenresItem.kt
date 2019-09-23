package gr.pchasapis.moviedb.model.parsers.genre

import com.google.gson.annotations.SerializedName

data class GenresItem(@SerializedName("name") val name: String? = null,
					  @SerializedName("id") val id: Int? = null)
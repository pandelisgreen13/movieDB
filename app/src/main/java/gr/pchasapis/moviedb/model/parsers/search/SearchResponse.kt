package gr.pchasapis.moviedb.model.parsers.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
	@SerialName("page") val page: Int? = null,
	@SerialName("total_pages") val totalPages: Int? = null,
	@SerialName("results") val searchResultsList: List<SearchItem>? = null,
	@SerialName("total_results") val totalResults: Int? = null
)

package gr.pchasapis.moviedb.model.parsers.search

import com.google.gson.annotations.SerializedName

data class SearchResponse(@SerializedName("page") val page: Int? = null,
						  @SerializedName("total_pages") val totalPages: Int? = null,
						  @SerializedName("results") val searchResultsList: List<SearchItem>? = null,
						  @SerializedName("total_results") val totalResults: Int? = null)
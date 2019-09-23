package gr.pchasapis.moviedb.model.parsers.common

import com.google.gson.annotations.SerializedName

open class CommonResponse {

    @SerializedName("status_code")
    val statusCode: Int? = 0

    @SerializedName("status_message")
    val statusMessage: String? = ""
}
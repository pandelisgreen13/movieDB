package gr.pchasapis.moviedb.model.parsers.common


import kotlinx.serialization.SerialName

open class CommonResponse {

    @SerialName("status_code")
    val statusCode: Int? = 0

    @SerialName("status_message")
    val statusMessage: String? = ""
}

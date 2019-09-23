package gr.pchasapis.moviedb.model.common

data class DataResult<out T>(
        val data: T? = null,
        val throwable: Throwable? = null
)
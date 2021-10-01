package gr.pchasapis.moviedb.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TheatreDataModel(val list: List<MovieDataModel> = arrayListOf()) : Parcelable
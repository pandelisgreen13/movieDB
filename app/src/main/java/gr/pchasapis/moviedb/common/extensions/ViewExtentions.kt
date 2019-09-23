package gr.pchasapis.moviedb.common.extensions

import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso
import gr.pchasapis.moviedb.R


fun AppCompatImageView.loadUrl(url: String?) {
    if (url != null) {
        Picasso.get().load(url).placeholder(R.mipmap.ic_launcher).into(this)
    } else {
        Picasso.get().load(R.mipmap.ic_launcher).into(this)
    }
}


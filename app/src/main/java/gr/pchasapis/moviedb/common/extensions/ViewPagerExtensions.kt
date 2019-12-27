package gr.pchasapis.moviedb.common.extensions

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.setMargins(offsetPx: Int, pageMarginPx: Int) {
    this.setPageTransformer { page, position ->
        val viewPager = page.parent.parent as ViewPager2
        val offset = position * -(2 * offsetPx + pageMarginPx)
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            page.translationX = offset
        } else {
            page.translationY = offset
        }
    }
}

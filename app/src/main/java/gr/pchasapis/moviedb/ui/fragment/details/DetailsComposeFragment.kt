package gr.pchasapis.moviedb.ui.fragment.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.MovieDBTheme

@ExperimentalAnimationGraphicsApi
class DetailsComposeFragment : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieDBTheme {
                MyApp()
            }
        }
    }
}

@ExperimentalAnimationGraphicsApi
@Composable
private fun MyApp() {
    Surface(color = MaterialTheme.colors.primary) {
        Row {
            Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null
            )

            Text(
                    text = "ToolbarTitle",
                    color = Color.White,
                    fontSize = 18.sp,
                    maxLines = 1,
            )

            val image = AnimatedImageVector.animatedVectorResource(R.drawable.drawable_favourite_selector)
            val atEnd by remember { mutableStateOf(false) }

            Icon(
                    painter = rememberAnimatedVectorPainter(image, atEnd),
                    contentDescription = null
            )
        }
    }
}

@ExperimentalAnimationGraphicsApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieDBTheme {
        MyApp()
    }
}

@Composable
private fun getImage(thumbnail: String?): Painter {
    return if (thumbnail != null && thumbnail.contains("null")) {
        painterResource(id = R.mipmap.ic_launcher)
    } else {
        rememberImagePainter(thumbnail)
    }
}

@Composable
private fun ComposeText(text: String = "-",
                        maxLines: Int = 1,
                        modifier: Modifier) {
    Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier
    )
}

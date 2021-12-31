package gr.pchasapis.moviedb.ui.fragment.details

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import dagger.hilt.android.AndroidEntryPoint
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.details.ui.theme.PrimaryDark

@AndroidEntryPoint
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

@Composable
private fun MyApp() {
    Surface(
            color = PrimaryDark,
            modifier = Modifier.fillMaxSize()
    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
        ) {

            ToolbarCompose()
            Spacer(modifier = Modifier.size(14.dp))
            ContentCompose()
        }
    }
}

@Composable
fun ContentCompose() {

    Row {

        Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                        .padding(end = 10.dp)
                        .height(120.dp)
        )

        Column(
                modifier = Modifier.height(120.dp),
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            ComposeText("plaaaaaaa" ?: "-", 6, Modifier.padding(bottom = 2.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                ComposeText(
                        text = stringResource(R.string.details_genre),
                        modifier = Modifier.padding(bottom = 2.dp, end = 4.dp)
                )

                ComposeText(
                        text = "Horror" ?: "-",
                        maxLines = 2,
                        modifier = Modifier.padding(bottom = 2.dp)
                )

            }
        }

    }

}

@Composable
fun ToolbarCompose() {

    Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth()
    ) {

        Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Color.White
        )

        Text(
                text = "ToolbarTitle",
                color = Color.White,
                fontSize = 18.sp,
                maxLines = 1,
        )

        Icon(
                painter = painterResource(id = R.drawable.ic_favourite_un_selected),
                contentDescription = null,
                tint = Color.Unspecified
        )
    }
}

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

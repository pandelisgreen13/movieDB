@file:OptIn(ExperimentalMaterial3Api::class)

package gr.pchasapis.moviedb.ui.fragment.home.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.model.data.HomeDataModel
import gr.pchasapis.moviedb.ui.compose.MovieDBTheme
import gr.pchasapis.moviedb.ui.fragment.favourite.card.FavouriteList

@Composable
fun HomeScreen(list: List<HomeDataModel>) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)) {
        ToolbarCenterAligned {

        }

        SearchView()

        FavouriteList(
            messages = list,
            onItemClicked = {}
        )

    }

}

@Preview
@Composable
fun PreviewHome() {
    MovieDBTheme {
        val list = arrayListOf(
            HomeDataModel(
                ratings = "5",
                title = "Avengers",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            ),
            HomeDataModel(
                ratings = "5",
                title = "Avengers222",
                releaseDate = "25/5/2019"
            )
        )
        HomeScreen(list = list)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolbarCenterAligned(onClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.home_toolbar_title)) },
        actions = {
            IconButton(onClick = {
                onClick.invoke()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_theatre),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
private fun SearchView() {
    var text by remember { mutableStateOf("Hello") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        ),
    )
}

@Preview
@Composable
fun PreviewToolbar() {
    MovieDBTheme {
        ToolbarCenterAligned({})
    }
}

@Preview
@Composable
fun PreviewSearch() {
    MovieDBTheme {
        SearchView()
    }
}
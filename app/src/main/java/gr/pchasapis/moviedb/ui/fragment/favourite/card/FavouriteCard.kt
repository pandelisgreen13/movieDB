package gr.pchasapis.moviedb.ui.fragment.favourite.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import gr.pchasapis.moviedb.R
import gr.pchasapis.moviedb.ui.compose.CardContent

@Composable
private fun Favourite(name: String) {
    Card(
            backgroundColor = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                    .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        FavouriteContent(name)
    }
}

@Composable
fun FavouriteContent(name: String) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
            modifier = Modifier
                    .padding(10.dp)
                    .animateContentSize(
                            animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                            )
                    )
    ) {
        Column(
                modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
        ) {

            Icon(
                    imageVector =  Icons.Filled.ExpandLess,
                    contentDescription = stringResource(id = R.string.app_name)

            )
            
            Text(text = "Hello, ")
            Text(
                    text = name,
                    style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                    )
            )
            if (expanded) {
                Text(
                        text = ("Composem ipsum color sit lazy, " +
                                "padding theme elit, sed do bouncy. ").repeat(4),
                )
            }
        }
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) {
                        stringResource(R.string.show_less)
                    } else {
                        stringResource(R.string.show_more)
                    }

            )
        }
    }
}

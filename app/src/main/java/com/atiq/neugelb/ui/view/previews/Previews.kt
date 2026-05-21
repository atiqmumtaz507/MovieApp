package com.atiq.neugelb.ui.view.previews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.atiq.neugelb.ui.theme.NeuGelbTheme
import com.atiq.neugelb.ui.view.common.PagerIndicator
import com.atiq.neugelb.ui.view.common.StarRating
import com.atiq.neugelb.ui.view.common.UserPercentageView
import com.atiq.neugelb.ui.view.main.Filter
import com.atiq.neugelb.ui.view.main.FiltersRow
import com.atiq.neugelb.ui.view.main.MainScreen
import com.atiq.neugelb.ui.view.main.MainScreenState

@Preview(showBackground = true, backgroundColor = 0x00F6F1F1)
@Composable
fun MainScreenPreview() {
    NeuGelbTheme {
        val state = remember { mutableStateOf(MainScreenState()) }
        MainScreen(
            state,
            pagerPlayTogggleClicked = {},
            onItemClicked = {},
            onSearchButtonClicked = {},
            onMovieListItemShown = {},
            onFilterItemClick = {}) {}
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F6F6)
@Composable
fun PagerIndicatorPreview(modifier: Modifier = Modifier) {
    NeuGelbTheme {
        PagerIndicator(
            3,
            10,
            Color.Black,
            Color.Red,
            Modifier.wrapContentSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserPercentageViewPreView() {
    NeuGelbTheme {
        UserPercentageView(progress = 50.0)
    }
}

@Preview(showBackground = true)
@Composable
fun StarRatingPreView() {
    NeuGelbTheme {
        StarRating(
            7.65
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterPreView() {
    NeuGelbTheme {
        FiltersRow(
            Modifier.fillMaxWidth(),
            MainScreenState(
                genres = mapOf(
                    "filter1" to 1,
                    "filter2" to 2,
                    "filter3" to 3,
                    "filter4" to 4
                ),
                selectedGenreId = 1
            )
        ) { }
    }
}
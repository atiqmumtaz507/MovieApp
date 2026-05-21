package com.atiq.neugelb.ui.view.main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Filter(
    modifier: Modifier = Modifier,
    filterName: String,
    filterId: Int,
    isSelected: Boolean,
    onClick: (Int) -> Unit
) {
    FilterChip(
        isSelected,
        onClick = { onClick(filterId) },
        label = {
            Text(filterName)
        },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.Red,
            labelColor = Color.White,
            selectedLabelColor = Color.Yellow,
            selectedContainerColor = Color.Black
        )
    )
}

@Composable
fun FiltersRow(
    modifier: Modifier = Modifier,
    screenState: MainScreenState,
    onItemClick: (Int) -> Unit
) {
    if (screenState.genres.isNotEmpty()) {
        val scroll = rememberScrollState()
        Row(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .horizontalScroll(scroll),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {

            val sortedGenres = remember(screenState.genres, screenState.selectedGenreId) {
                if(screenState.selectedGenreId == -1) screenState.genres.toList()
                else screenState.genres.toList().sortedByDescending { one ->
                    one.second == screenState.selectedGenreId
                }
            }
            sortedGenres.forEach {
                Filter(
                    filterName = it.first,
                    filterId = it.second,
                    isSelected = it.second == screenState.selectedGenreId
                ) { selectedFilterId ->
                    onItemClick(selectedFilterId)
                }
            }
        }
    }
}
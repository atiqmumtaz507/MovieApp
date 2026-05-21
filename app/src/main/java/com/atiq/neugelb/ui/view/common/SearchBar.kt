package com.atiq.neugelb.ui.view.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    hintText: String,
    textStyle: TextStyle,
    onFocusChanged: (Boolean) -> Unit,
    onValueChanged: (String) -> Unit,
    onClearButtonClicked: () -> Unit,
    onSearchButtonClicked: (String) -> Unit
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onValueChanged,
        modifier = modifier.onFocusChanged { focusState ->
            onFocusChanged(focusState.isFocused)
        },
        textStyle = textStyle,
        placeholder = { Text(hintText) },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                IconButton(onClick = onClearButtonClicked, Modifier.padding(end = 10.dp)) {
                    Icon(
                        Icons.Default.Clear,
                        null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                null,
                Modifier.padding(start = 10.dp)
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearchButtonClicked(searchText)
        }),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Red,
            unfocusedContainerColor = Color.Red,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            cursorColor = Color.White,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White
        )
    )
}
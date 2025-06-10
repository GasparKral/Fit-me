package es.gaspardev.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <E> AutoCompleteTextField(
    initialValue: String = "",
    onValueChange: (String) -> Unit = {},
    onItemSelected: (E) -> Unit = {},
    options: List<E> = emptyList(),
    maxShowElements: Int = 10,
    getFilterableText: (E) -> String,
    modifier: Modifier = Modifier,
    dropDownModifier: Modifier = Modifier,
    dropDownHeight: Dp = 400.dp,
    label: @Composable () -> Unit = {},
    placeHolder: @Composable () -> Unit = {},
    leadingIcon: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    dropDownComponent: @Composable (E) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    var filterQuery by remember { mutableStateOf(initialValue) }
    var isFocused by remember { mutableStateOf(false) }
    var showDropdown by remember { mutableStateOf(false) }

    val scroll = rememberScrollState()

    // Controlar cuándo mostrar el dropdown
    LaunchedEffect(isFocused, filterQuery) {
        showDropdown = isFocused && (filterQuery.isNotEmpty() || options.isNotEmpty())
    }

    val filteredOptions = remember(filterQuery, options) {
        if (filterQuery.isEmpty()) {
            options.take(maxShowElements)
        } else {
            options.filter {
                getFilterableText(it).contains(filterQuery, ignoreCase = true)
            }.take(maxShowElements)
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = filterQuery,
            onValueChange = {
                filterQuery = it
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Escape && keyEvent.type == KeyEventType.KeyDown) {
                        showDropdown = false
                        true
                    } else {
                        false
                    }
                },
            label = label,
            placeholder = placeHolder,
            leadingIcon = leadingIcon,
            trailingIcon = {
                if (filterQuery.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            filterQuery = ""
                            onValueChange("")
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Borrar texto",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    trailingIcon()
                }
            },
            isError = isError,
            enabled = enabled,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colors.primary,
                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                errorBorderColor = MaterialTheme.colors.error,
                backgroundColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        AnimatedVisibility(
            visible = showDropdown && (filteredOptions.isNotEmpty() || filterQuery.isNotEmpty()),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = dropDownModifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .heightIn(max = dropDownHeight)
                    .padding(top = 4.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 0.dp
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scroll)
                    ) {
                        if (filteredOptions.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay coincidencias",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        } else {
                            filteredOptions.forEachIndexed { index, item ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            filterQuery = getFilterableText(item)
                                            onValueChange(getFilterableText(item))
                                            onItemSelected(item)
                                            showDropdown = false
                                        }
                                        .background(
                                            color = Color.Transparent,
                                            shape = RoundedCornerShape(
                                                topStart = if (index == 0) 12.dp else 0.dp,
                                                topEnd = if (index == 0) 12.dp else 0.dp,
                                                bottomStart = if (index == filteredOptions.lastIndex) 12.dp else 0.dp,
                                                bottomEnd = if (index == filteredOptions.lastIndex) 12.dp else 0.dp
                                            )
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                color = Color.Transparent
                                            )
                                            .hoverable(
                                                interactionSource = remember { MutableInteractionSource() }
                                            )
                                    ) {
                                        dropDownComponent(item)
                                    }
                                }

                                if (index < filteredOptions.lastIndex) {
                                    Divider(
                                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }

                    if (scroll.maxValue > 0) {
                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(scroll),
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp, top = 8.dp, bottom = 8.dp)
                                .width(6.dp),
                            style = LocalScrollbarStyle.current.copy(
                                hoverColor = MaterialTheme.colors.primary.copy(alpha = 0.8f),
                                unhoverColor = MaterialTheme.colors.primary.copy(alpha = 0.4f),
                                thickness = 6.dp
                            )
                        )
                    }
                }
            }
        }
    }
}
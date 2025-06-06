package es.gaspardev.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.actions
import fit_me.composeapp.generated.resources.more_options
import org.jetbrains.compose.resources.stringResource

@Composable
fun DropdownMenuButton(
    items: List<@Composable () -> Unit>,
    onItemSelected: (Int, (Boolean) -> Unit) -> Unit,
    icon: @Composable () -> Unit = {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = stringResource(Res.string.more_options)
        )
    },
    label: (@Composable () -> Unit)? = {
        Text(
            text = stringResource(Res.string.actions),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Divider()
    }
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            icon.invoke()
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            label?.invoke()
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(index) { value -> expanded = value }
                    }
                ) { item.invoke() }
            }
        }
    }
}
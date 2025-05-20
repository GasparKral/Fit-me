package es.gaspardev.layout.nutrition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuButton(
   items: List<String>,
   onItemSelected: (Int) -> Unit
) {
   var expanded by remember { mutableStateOf(false) }

   Box {
      IconButton(onClick = { expanded = true }) {
         Icon(Icons.Default.MoreVert, contentDescription = "More options")
      }

      DropdownMenu(
         expanded = expanded,
         onDismissRequest = { expanded = false }
      ) {
         Text(
            text = "Actions",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
         )
         Divider()
         items.forEachIndexed { index, item ->
            DropdownMenuItem(
               onClick = {
                  onItemSelected(index)
                  expanded = false
               }
            ) { Text(item) }
         }
      }
   }
}
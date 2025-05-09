package es.gaspardev.layout.Nutrition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlanBadge(icon: ImageVector, text: String) {
   Surface(
      shape = RoundedCornerShape(16.dp),
      color = Color.Transparent,
      border = BorderStroke(1.dp, MaterialTheme.colors.primary)
   ) {
      Row(
         modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
         verticalAlignment = Alignment.CenterVertically
      ) {
         Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp)
         )
         Spacer(Modifier.width(4.dp))
         Text(text, fontSize = 12.sp)
      }
   }
}
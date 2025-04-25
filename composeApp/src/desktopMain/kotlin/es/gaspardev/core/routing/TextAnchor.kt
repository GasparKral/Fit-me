package es.gaspardev.core.routing

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.gaspardev.core.Routing.Route
import es.gaspardev.core.Routing.RouterController
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun TextAnchor(
    href: Route,
    controller: RouterController,
    modifier: Modifier = Modifier,
    text: String
) {

    Text(
        modifier = modifier.clickable { controller.navigateTo(href) },
        text = text,
    )
}
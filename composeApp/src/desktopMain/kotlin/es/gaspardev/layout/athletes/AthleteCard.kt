package es.gaspardev.layout.athletes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.gaspardev.components.LastActiveText
import es.gaspardev.components.LayoutDirection
import es.gaspardev.components.ProgressBar
import es.gaspardev.components.UserAvatar
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.enums.StatusState
import es.gaspardev.icons.FitMeIcons
import fit_me.composeapp.generated.resources.Res
import fit_me.composeapp.generated.resources.active
import fit_me.composeapp.generated.resources.age_years
import fit_me.composeapp.generated.resources.progress
import org.jetbrains.compose.resources.stringResource

@Composable
fun AthleteCard(
    athlete: Athlete,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = 2.dp
    ) {
        Column {
            // Barra superior con el deporte
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Deporte con icono
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(FitMeIcons.Athlets, "", tint = MaterialTheme.colors.onPrimary)
                    Text(
                        text = athlete.sex.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colors.background,
                    border = BorderStroke(1.dp, MaterialTheme.colors.primary)
                ) {
                    if (athlete.user.status.state == StatusState.ACTIVE) {
                        Text(
                            text = stringResource(Res.string.active),
                            style = MaterialTheme.typography.overline,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    } else {
                        LastActiveText(athlete.user.status.lastTimeActive, textColor = MaterialTheme.colors.primary)
                    }
                }

            }

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                UserAvatar(athlete.user, LayoutDirection.Vertical, subtitleContent = {
                    Text(
                        text = athlete.user.email,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                })

                // Edad
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.age_years).format(athlete.age),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                // Línea divisoria
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = MaterialTheme.colors.primary
                )

                // Estadísticas
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProgressBar(
                        athlete.getOverallProgression(),
                        label = { Text(stringResource(Res.string.progress), fontWeight = FontWeight.Bold) })
                }
            }
        }
    }
}
package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtAvatar
import com.bettertravel.ui.designsystem.atoms.BtAvatarRing
import com.bettertravel.ui.designsystem.atoms.BtDot
import com.bettertravel.ui.designsystem.atoms.BtDurationTag
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.atoms.BtMarkerStyle
import com.bettertravel.ui.designsystem.atoms.BtNumberMarker
import com.bettertravel.ui.designsystem.atoms.BtRating
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtGradients
import com.bettertravel.ui.designsystem.theme.BtShapes
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.betty
import com.bettertravel.ui.resources.ic_medal_star
import com.bettertravel.ui.resources.ic_reorder
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BtWaypointCard(
    order: Int,
    markerStyle: BtMarkerStyle,
    title: String,
    description: String,
    duration: String,
    rating: String,
    reviews: String,
    departure: String?,
    thumbnail: DrawableResource,
    modifier: Modifier = Modifier,
    warm: Boolean = false,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xs, BtShapes.md)
                .clip(BtShapes.md)
                .background(colors.surface)
                .padding(BtSpacing.m),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BtNumberMarker(order, style = markerStyle)
        Image(
            painter = painterResource(thumbnail),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceContainerMax),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = BetterTravelTheme.typography.titleSmall,
                    color = colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                BtDurationTag(duration, warm = warm)
            }
            Text(
                text = description,
                style = BetterTravelTheme.typography.bodySmall,
                color = colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BtRating(rating = rating, reviews = reviews)
                if (departure != null) {
                    Text(
                        text = "· $departure",
                        style = BetterTravelTheme.typography.labelMedium,
                        color = colors.textTertiary,
                    )
                }
            }
        }
        BtIcon(Res.drawable.ic_reorder, tint = colors.textTertiary, size = 14.dp)
    }
}

@Composable
fun BtCityCard(
    name: String,
    routes: String,
    sights: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Column(
        modifier =
            modifier
                .widthIn(min = 128.dp)
                .clip(BtShapes.md)
                .background(if (selected) colors.primaryContainer.copy(alpha = 0.3f) else colors.surfaceContainer)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(BtSpacing.m),
        verticalArrangement = Arrangement.spacedBy(BtSpacing.xs),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.s), verticalAlignment = Alignment.CenterVertically) {
            Text(text = name, style = BetterTravelTheme.typography.headlineSemiBold, color = colors.textPrimary)
            if (selected) {
                BtDot(colors.coral, size = 8.dp)
            }
        }
        Text(text = routes, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
        Text(text = sights, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
    }
}

@Composable
fun BtInsightCard(
    eyebrow: String,
    tag: String,
    quote: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xs, BtShapes.md)
                .clip(BtShapes.md)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
    ) {
        BtAvatar(image = Res.drawable.betty, size = 48.dp, ring = BtAvatarRing.Companion)
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = eyebrow, style = BetterTravelTheme.typography.eyebrow, color = colors.primary)
                BtDot(colors.coral)
                Text(text = tag, style = BetterTravelTheme.typography.bodySmall, color = colors.textTertiary)
            }
            Text(text = quote, style = BetterTravelTheme.typography.bodyLarge, color = colors.textPrimary)
        }
    }
}

@Composable
fun BtSummaryBanner(
    title: String,
    subtitle: String,
    icon: DrawableResource,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xs, BtShapes.xxl)
                .clip(BtShapes.xxl)
                .background(colors.surfaceContainer)
                .padding(BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colors.brand.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(icon, tint = colors.primary, size = 22.dp)
        }
        Column {
            Text(text = title, style = BetterTravelTheme.typography.headlineSemiBold, color = colors.textPrimary)
            Text(text = subtitle, style = BetterTravelTheme.typography.bodySmall, color = colors.textSecondary)
        }
    }
}

@Composable
fun BtXpBanner(
    xp: String,
    level: String,
    title: String,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .btShadow(BtElevation.Xs, BtShapes.md)
                .clip(BtShapes.md)
                .background(colors.surfaceContainerHighest.copy(alpha = 0.7f))
                .padding(BtSpacing.l),
        horizontalArrangement = Arrangement.spacedBy(BtSpacing.l),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(BtGradients.xpBadge),
            contentAlignment = Alignment.Center,
        ) {
            BtIcon(Res.drawable.ic_medal_star, tint = colors.textOnPrimary, size = 22.dp)
        }
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.xs)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.s),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = xp, style = BetterTravelTheme.typography.eyebrow, color = colors.coralText)
                BtDot(colors.coralText)
                Text(text = level, style = BetterTravelTheme.typography.labelMedium, color = colors.textSecondary)
            }
            Text(text = title, style = BetterTravelTheme.typography.button, color = colors.textPrimary)
        }
    }
}

@Composable
fun BtCompanionPrompt(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .btShadow(BtElevation.Lg, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(start = 15.dp, end = 9.dp, top = 9.dp, bottom = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(text = title, style = BetterTravelTheme.typography.labelMedium, color = colors.textPrimary)
            Row(
                horizontalArrangement = Arrangement.spacedBy(BtSpacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = subtitle, style = BetterTravelTheme.typography.labelSmall, color = colors.primary)
                BtDot(colors.brand)
            }
        }
        BtAvatar(image = Res.drawable.betty, size = 36.dp, ring = BtAvatarRing.Border)
    }
}

@Composable
fun BtAudioGuideWidget(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .widthIn(max = 280.dp)
                .btShadow(BtElevation.Xl, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .clickable(role = Role.Button, onClick = onClick)
                .padding(start = BtSpacing.m, end = BtSpacing.s, top = 6.dp, bottom = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(text = title, style = BetterTravelTheme.typography.labelMedium, color = colors.coralText)
            Text(
                text = subtitle,
                style = BetterTravelTheme.typography.bodySmall,
                color = colors.textSecondary,
                textAlign = TextAlign.End,
            )
        }
        BtAvatar(image = Res.drawable.betty, size = 44.dp, ring = BtAvatarRing.Companion)
    }
}

@Preview
@Composable
private fun BtWaypointCardPreview() {
    BtPreview {
        Column(verticalArrangement = Arrangement.spacedBy(BtSpacing.m)) {
            BtWaypointCard(
                order = 1,
                markerStyle = BtMarkerStyle.Primary,
                title = "Old Town Market Square",
                description = "Historic landmark, vibrant cafés",
                duration = "30 min",
                rating = "4.9",
                reviews = "1.2k",
                departure = "Departure 10:00 AM",
                thumbnail = Res.drawable.betty,
            )
            BtWaypointCard(
                order = 3,
                markerStyle = BtMarkerStyle.Coral,
                title = "Royal Castle & Gardens",
                description = "Baroque residence with gardens",
                duration = "25 min",
                rating = "4.8",
                reviews = "980",
                departure = null,
                thumbnail = Res.drawable.betty,
                warm = true,
            )
        }
    }
}

@Preview
@Composable
private fun BtCityCardPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BtCityCard("Warsaw", "4 Routes", "16 Sights logged", selected = true, onClick = {})
            BtCityCard("Prague", "2 Routes", "9 Sights logged", selected = false, onClick = {})
        }
    }
}

@Preview
@Composable
private fun BtInsightCardPreview() {
    BtPreview {
        BtInsightCard(
            eyebrow = "BETTY SECRET",
            tag = "Historical flash",
            quote = "Did you know? During WWII, 90% of the cathedral was destroyed.",
        )
    }
}

@Preview
@Composable
private fun BtSummaryBannerPreview() {
    BtPreview {
        BtSummaryBanner(
            title = "18 Moments captured",
            subtitle = "Across 2 countries • 42.6 km walked",
            icon = Res.drawable.ic_medal_star,
        )
    }
}

@Preview
@Composable
private fun BtXpBannerPreview() {
    BtPreview {
        BtXpBanner(xp = "+150 XP EARNED", level = "Level 4", title = "'Old Town Explorer' unlocked!")
    }
}

@Preview
@Composable
private fun BtCompanionPromptPreview() {
    BtPreview {
        BtCompanionPrompt(title = "Warsaw secret?", subtitle = "Ask Betty", onClick = {})
    }
}

@Preview
@Composable
private fun BtAudioGuideWidgetPreview() {
    BtPreview {
        BtAudioGuideWidget(title = "Audio guide ready!", subtitle = "Tap to listen together", onClick = {})
    }
}

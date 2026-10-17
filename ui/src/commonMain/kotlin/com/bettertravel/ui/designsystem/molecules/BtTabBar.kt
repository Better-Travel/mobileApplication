package com.bettertravel.ui.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bettertravel.ui.designsystem.atoms.BtIcon
import com.bettertravel.ui.designsystem.preview.BtPreview
import com.bettertravel.ui.designsystem.theme.BetterTravelTheme
import com.bettertravel.ui.designsystem.theme.BtElevation
import com.bettertravel.ui.designsystem.theme.BtSpacing
import com.bettertravel.ui.designsystem.theme.btShadow
import com.bettertravel.ui.resources.Res
import com.bettertravel.ui.resources.ic_tab_discover
import com.bettertravel.ui.resources.ic_tab_memories
import com.bettertravel.ui.resources.ic_tab_route_map
import org.jetbrains.compose.resources.DrawableResource

data class BtTab(
    val label: String,
    val icon: DrawableResource,
)

@Composable
fun BtTabBar(
    tabs: List<BtTab>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = BetterTravelTheme.colors
    Row(
        modifier =
            modifier
                .widthIn(max = 448.dp)
                .fillMaxWidth()
                .btShadow(BtElevation.Lg, CircleShape)
                .clip(CircleShape)
                .background(colors.glassStrong)
                .border(1.dp, colors.glassBorder, CircleShape)
                .padding(horizontal = BtSpacing.l, vertical = BtSpacing.xs),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEachIndexed { index, tab ->
            BtTabItem(
                tab = tab,
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
            )
        }
    }
}

@Composable
private fun BtTabItem(
    tab: BtTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val colors = BetterTravelTheme.colors
    val tint = if (selected) colors.brand else colors.textSecondary
    Column(
        modifier =
            Modifier
                .heightIn(min = 56.dp)
                .widthIn(min = 56.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable(role = Role.Tab, onClick = onClick)
                .padding(horizontal = BtSpacing.s),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
    ) {
        BtIcon(tab.icon, tint = tint, size = 20.dp)
        Text(
            text = tab.label,
            style = BetterTravelTheme.typography.labelMedium,
            color = tint,
            softWrap = false,
            overflow = TextOverflow.Visible,
        )
    }
}

@Preview
@Composable
private fun BtTabBarPreview() {
    BtPreview {
        BtTabBar(
            tabs =
                listOf(
                    BtTab("Discover", Res.drawable.ic_tab_discover),
                    BtTab("Route", Res.drawable.ic_tab_route_map),
                    BtTab("Memories", Res.drawable.ic_tab_memories),
                ),
            selectedIndex = 1,
            onTabSelected = {},
        )
    }
}

@Preview
@Composable
private fun BtTabItemPreview() {
    BtPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(BtSpacing.l)) {
            BtTabItem(BtTab("Route", Res.drawable.ic_tab_route_map), selected = true, onClick = {})
            BtTabItem(BtTab("Memories", Res.drawable.ic_tab_memories), selected = false, onClick = {})
        }
    }
}

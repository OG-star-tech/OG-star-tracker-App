package og.ogstartracker.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.R
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle12ItalicBold
import og.ogstartracker.ui.theme.textStyle24Bold
import og.ogstartracker.utils.segmentedShadow

@Composable
fun ConnectionCard(
	connected: Boolean,
	haveLocationPermission: Boolean,
	haveNotificationPermission: Boolean,
	onCardClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val interactionSource = remember { MutableInteractionSource() }
	Column(
		modifier = modifier
			.fillMaxWidth()
			.segmentedShadow(AppTheme.colorScheme.shadow)
			.padding(DimensNormal100)
			.clip(ShapeNormal)
			.background(color = ColorBackground)
			.clickable(
				interactionSource = interactionSource,
				indication = rememberRipple(
					color = ColorPrimary,
					bounded = true,
				),
				enabled = !haveLocationPermission || !haveNotificationPermission || !connected,
			) {
				onCardClick()
			}
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_wifi_check.takeIf {
					connected
				} ?: R.drawable.ic_wifi_cancel),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.connection_title).uppercase(),
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
				Text(
					text = if (connected) {
						stringResource(id = R.string.connection_connected)
					} else {
						stringResource(id = R.string.connection_not_connected)
					},
					style = textStyle24Bold,
					color = AppTheme.colorScheme.primary
				)
				if (!haveLocationPermission) {
					Text(
						text = stringResource(id = R.string.location_hint),
						style = textStyle12ItalicBold,
						color = ColorSecondary
					)
				}
				if (!haveNotificationPermission) {
					Text(
						text = stringResource(id = R.string.notification_hint),
						style = textStyle12ItalicBold,
						color = ColorSecondary
					)
				}
				if (!connected) {
					Text(
						text = stringResource(id = R.string.connection_hint),
						style = textStyle12ItalicBold,
						color = ColorSecondary
					)
				}
			}

			Image(
				painter = painterResource(
					id = R.drawable.tracker_conencted.takeIf { connected } ?: R.drawable.tracker_empty
				),
				contentDescription = null,
				modifier = Modifier
					.padding(end = DimensNormal100)
					.size(56.dp, 60.dp)
			)
		}
	}
}

@Preview
@Composable
fun ConnectionCardPreview() {
	AppTheme {
		Column {
			ConnectionCard(
				connected = false,
				onCardClick = {},
				haveLocationPermission = false,
				haveNotificationPermission = true,
			)
			ConnectionCard(
				connected = true,
				onCardClick = {},
				haveLocationPermission = false,
				haveNotificationPermission = false,
			)

			ConnectionCard(
				connected = false,
				onCardClick = {},
				haveLocationPermission = true,
				haveNotificationPermission = true,
			)
			ConnectionCard(
				connected = true,
				onCardClick = {},
				haveLocationPermission = true,
				haveNotificationPermission = true,
			)
		}
	}
}
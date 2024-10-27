package og.ogstartracker.ui.components.cards

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import og.ogstartracker.Constants
import og.ogstartracker.R
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.models.TrackingMode
import og.ogstartracker.ui.components.common.CustomSwitch
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.utils.segmentedShadow

@Composable
fun SiderealCard(
	enabled: Boolean,
	trackingMode: String,
	active: Boolean,
	onCheckChanged: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	hemisphere: String,
) {
	val infiniteTransition = rememberInfiniteTransition(label = "")
	val color by infiniteTransition.animateColor(
		initialValue = AppTheme.colorScheme.shadow,
		targetValue = AppTheme.colorScheme.primary,
		animationSpec = infiniteRepeatable(
			animation = tween(500, 0, FastOutLinearInEasing),
			repeatMode = RepeatMode.Reverse
		), label = ""
	)

	Column(
		modifier = modifier
			.fillMaxWidth()
			.segmentedShadow(color.takeIf { active } ?: AppTheme.colorScheme.shadow)
			.padding(DimensNormal100)
			.clip(ShapeNormal)
			.background(color = ColorBackground)
			.alpha(Constants.Percent.PERCENT_100.takeIf { enabled } ?: Constants.Percent.PERCENT_50)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_tracking),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.sidereal_tracking_title).uppercase(),
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = stringResource(
						id = R.string.sidereal_tracking_subtitle, trackingMode.uppercase(), hemisphere.uppercase()
					),
					style = textStyle10ItalicBold,
					color = ColorSecondary,
				)
			}

			CustomSwitch(
				checked = active,
				onCheckChange = onCheckChanged,
				modifier = Modifier.padding(end = DimensNormal100),
				enabled = enabled
			)
		}
	}
}

@Preview
@Composable
fun SiderealCardPreview() {
	AppTheme {
		Column {
			SiderealCard(
				enabled = true,
				trackingMode = stringResource(TrackingMode.SIDEREAL.text),
				active = true,
				onCheckChanged = {},
				hemisphere = stringResource(Hemisphere.NORTH.text),
			)
			SiderealCard(
				enabled = true,
				trackingMode = stringResource(TrackingMode.SIDEREAL.text),
				active = false,
				onCheckChanged = {},
				hemisphere = stringResource(Hemisphere.NORTH.text),
			)
		}
	}
}
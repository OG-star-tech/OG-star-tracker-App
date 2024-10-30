package og.ogstartracker.ui.components.cards

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import og.ogstartracker.Constants
import og.ogstartracker.R
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal125
import og.ogstartracker.ui.theme.DimensNormal150
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.DimensSmall25
import og.ogstartracker.ui.theme.DimensSmall50
import og.ogstartracker.ui.theme.DropDownWidth
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.SlewButtonHeight
import og.ogstartracker.ui.theme.textStyle10Bold
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.segmentedShadow

private val speeds = listOf(2, 8, 50, 100, 200, 400)
private const val VIBRATION_PERIOD = 1000L
private const val ROTATE_DURATION = 2000

@Composable
fun SlewControlCard(
	enabled: Boolean,
	selectedSpeed: Int,
	slewControlCommands: (SlewControlEvent) -> Unit,
	modifier: Modifier = Modifier,
) {
	var dropOpened by remember { mutableStateOf(false) }
	var leftButtonPressed by remember { mutableStateOf(false) }
	var rightButtonPressed by remember { mutableStateOf(false) }

	val infiniteTransition = rememberInfiniteTransition(label = "")
	val angle by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 360f,
		animationSpec = infiniteRepeatable(
			animation = tween(ROTATE_DURATION, 0, LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = ""
	)

	LaunchedEffect(leftButtonPressed, rightButtonPressed) {
		while (leftButtonPressed) {
			delay(VIBRATION_PERIOD)
			slewControlCommands(SlewControlEvent.TriggerVibration)
		}

		while (rightButtonPressed) {
			delay(VIBRATION_PERIOD)
			slewControlCommands(SlewControlEvent.TriggerVibration)
		}
	}

	Column(
		modifier = modifier
			.fillMaxWidth()
			.segmentedShadow(AppTheme.colorScheme.shadow)
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
				painter = painterResource(id = R.drawable.ic_rotate_360),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.slew_control_title).uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = stringResource(id = R.string.slew_control_subtitle),
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(id = R.string.slew_control_step_size).uppercase(),
				style = textStyle10Bold,
				color = AppTheme.colorScheme.secondary,
				modifier = Modifier
					.padding(start = GeneralIconSize + DimensNormal200)
					.weight(1f)
			)

			Box(
				modifier = Modifier.width(DropDownWidth),
				contentAlignment = Alignment.CenterEnd,
			) {
				Text(
					text = "${selectedSpeed}x",
					style = textStyle20Bold,
					color = AppTheme.colorScheme.primary,
					modifier = Modifier
						.padding(end = DimensNormal100)
						.clickable {
							dropOpened = true
						}
				)

				DropdownMenu(
					modifier = Modifier.width(DropDownWidth),
					expanded = dropOpened,
					onDismissRequest = { dropOpened = false }) {
					speeds.forEachIndexed { index, speed ->
						DropdownMenuItem(
							text = {
								Text(text = "${speed}x")
							},
							onClick = {
								dropOpened = false
								slewControlCommands(SlewControlEvent.NewSpeed(speeds[index]))
							}
						)
					}
				}
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(
				end = DimensNormal100,
				bottom = DimensNormal100,
				top = DimensNormal125
			),
		) {
			Text(
				text = stringResource(id = R.string.slew_control_move).uppercase(),
				style = textStyle10Bold,
				color = AppTheme.colorScheme.secondary,
				modifier = Modifier
					.padding(start = GeneralIconSize + DimensNormal200)
					.weight(1f)
			)

			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(end = DimensSmall100)
					.pointerInput(Unit) {
						detectTapGestures(
							onPress = {
								if (!rightButtonPressed) {
									leftButtonPressed = true
								}
								slewControlCommands(SlewControlEvent.RotateAnticlockwise)
								tryAwaitRelease()
								leftButtonPressed = false
								slewControlCommands(SlewControlEvent.Release)
							}
						)
					}
					.background(color = AppTheme.colorScheme.primary, shape = CircleShape)
					.padding(end = DimensSmall50)
					.padding(horizontal = DimensNormal150)
					.height(SlewButtonHeight)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_restore),
					contentDescription = null,
					modifier = Modifier
						.graphicsLayer {
							if (leftButtonPressed) {
								rotationZ = -angle
							}
						}
						.size(DimensNormal150)
						.padding(start = DimensSmall25)
				)
			}

			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier
					.padding(start = DimensSmall100)
					.pointerInput(Unit) {
						detectTapGestures(
							onPress = {
								if (!leftButtonPressed) {
									rightButtonPressed = true
								}
								slewControlCommands(SlewControlEvent.RotateClockwise)
								tryAwaitRelease()
								rightButtonPressed = false
								slewControlCommands(SlewControlEvent.Release)
							}
						)
					}
					.background(color = AppTheme.colorScheme.primary, shape = CircleShape)
					.padding(end = DimensSmall50)
					.padding(horizontal = DimensNormal150)
					.height(SlewButtonHeight)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_restore),
					contentDescription = null,
					modifier = Modifier
						.graphicsLayer {
							scaleX = -1f
							if (rightButtonPressed) {
								rotationZ = angle
							}
						}
						.size(DimensNormal150)
						.padding(start = DimensSmall25)
				)
			}
		}
	}
}

@Preview
@Composable
fun SlewControlCardPreview() {
	AppTheme {
		Column {
			SlewControlCard(
				slewControlCommands = {},
				enabled = true,
				selectedSpeed = 2,
			)
		}
	}
}
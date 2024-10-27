package og.ogstartracker.ui.components.cards

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.Constants
import og.ogstartracker.R
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.usecases.settings.SettingItem
import og.ogstartracker.ui.components.common.CustomSwitch
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.components.common.input.ActionInput
import og.ogstartracker.ui.screens.DashboardUiState
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal150
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.DimensSmall25
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle12Bold
import og.ogstartracker.ui.theme.textStyle14Bold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle16Regular
import og.ogstartracker.utils.segmentedShadow
import og.ogstartracker.utils.toHours
import og.ogstartracker.utils.toMinutes
import og.ogstartracker.utils.toSeconds
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneId

private val formatter = DecimalFormat("00")

@Composable
fun PhotoControlCard(
	uiState: DashboardUiState,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	notifyAboutChange: (SettingItem, Int?) -> Unit,
	modifier: Modifier = Modifier
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
			.segmentedShadow(color.takeIf { uiState.capturingActive } ?: AppTheme.colorScheme.shadow)
			.padding(DimensNormal100)
			.clip(ShapeNormal)
			.background(color = ColorBackground)
			.animateContentSize()
			.alpha(Constants.Percent.PERCENT_100.takeIf { uiState.wifiConnected } ?: Constants.Percent.PERCENT_50)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_camera_flip_outline),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.photo_control_title).uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = stringResource(id = R.string.photo_control_subtitle),
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}
		}

		Text(
			text = stringResource(id = R.string.photo_control_intervalometer).uppercase(),
			style = textStyle14Bold,
			color = AppTheme.colorScheme.secondary,
			modifier = Modifier.padding(horizontal = DimensNormal100)
		)

		Column(
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensSmall100)
		) {
			ActionInput(
				enabled = !uiState.capturingActive && uiState.wifiConnected,
				textFieldState = uiState.exposeTime,
				label = stringResource(id = R.string.photo_control_exposure_length),
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						text = stringResource(id = R.string.photo_control_sec),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.exposeTime.setNewState(it)
					notifyAboutChange(SettingItem.EXPOSURE_TIME, it.text.toIntOrNull())
				}
			)
			ActionInput(
				enabled = !uiState.capturingActive && uiState.wifiConnected,
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.frameCount,
				label = stringResource(id = R.string.photo_control_exposure_count),
				placeholder = "0",
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Number,
				onValueChange = {
					uiState.frameCount.setNewState(it)
					notifyAboutChange(SettingItem.EXPOSURE_COUNT, it.text.toIntOrNull())
				}
			)
		}

		Divider(
			modifier = Modifier.padding(vertical = DimensNormal100)
		)

		Text(
			text = stringResource(id = R.string.photo_control_dithering).uppercase(),
			style = textStyle14Bold,
			color = AppTheme.colorScheme.secondary,
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensSmall100)
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(top = DimensNormal100)
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.padding(start = DimensNormal100)
			) {
				Text(
					text = stringResource(id = R.string.photo_control_dithering_enable).uppercase(),
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = stringResource(id = R.string.photo_control_dithering_subtitle),
					style = textStyle10ItalicBold,
					color = ColorSecondary
				)
			}

			CustomSwitch(
				checked = uiState.ditheringEnabled,
				onCheckChange = {
					onPhotoControlEvent(PhotoControlEvent.DitheringActivation(!uiState.ditheringEnabled))
					notifyAboutChange(SettingItem.DITHER_ACTIVE, if (it) 1 else 0)
				},
				modifier = Modifier.padding(end = DimensNormal100),
				enabled = !uiState.capturingActive && uiState.wifiConnected,
			)
		}

		Column(
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensNormal100)
		) {
			ActionInput(
				enabled = !uiState.capturingActive && uiState.wifiConnected && uiState.ditheringEnabled,
				textFieldState = uiState.ditherFocalLength,
				label = stringResource(id = R.string.photo_control_focal_length),
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_mm),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.ditherFocalLength.setNewState(it)
					notifyAboutChange(SettingItem.FOCAL_LENGTH, it.text.toIntOrNull())
				}
			)
			ActionInput(
				enabled = !uiState.capturingActive && uiState.wifiConnected && uiState.ditheringEnabled,
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.ditherPixelSize,
				label = stringResource(id = R.string.photo_control_pixel_size),
				placeholder = "0",
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Decimal,
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_µm),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.ditherPixelSize.setNewState(it)
					notifyAboutChange(SettingItem.PIXEL_SIZE, it.text.toIntOrNull())
				}
			)
		}

		Divider(
			modifier = Modifier.padding(vertical = DimensNormal100)
		)

		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.padding(start = DimensNormal100)
			) {
				Text(
					text = stringResource(id = R.string.photo_control_disable_tracking_title).uppercase(),
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = stringResource(id = R.string.photo_control_disable_tracking_subtitle),
					style = textStyle10ItalicBold,
					color = ColorSecondary
				)
			}

			CustomSwitch(
				checked = uiState.stopTrackingEnabled,
				onCheckChange = {
					onPhotoControlEvent(PhotoControlEvent.StopTrackingActivation(!uiState.stopTrackingEnabled))
					notifyAboutChange(SettingItem.STOP_TRACKING, if (it) 1 else 0)
				},
				modifier = Modifier.padding(end = DimensNormal100),
				enabled = !uiState.capturingActive && uiState.wifiConnected,
			)
		}

		Divider(
			modifier = Modifier.padding(vertical = DimensNormal100)
		)

		if (uiState.capturingActive) {
			CaptureInfo(uiState = uiState)
		}

		Row(
			horizontalArrangement = Arrangement.spacedBy(DimensNormal100),
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(bottom = DimensNormal100)
		) {
			Button(
				onClick = {
					onPhotoControlEvent(PhotoControlEvent.StartCapture)
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier
					.weight(1f)
					.height(40.dp),
				contentPadding = PaddingValues(horizontal = DimensSmall100),
				enabled = !uiState.capturingActive && uiState.wifiConnected && uiState.arePhotoControlInputsValid(),
			) {
				Text(
					text = stringResource(id = R.string.photo_control_start_capture).uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
				Icon(
					painter = painterResource(id = R.drawable.ic_camera),
					contentDescription = null,
					modifier = Modifier
						.size(DimensNormal150)
						.padding(start = DimensSmall25)
				)
			}

			Button(
				onClick = { onPhotoControlEvent(PhotoControlEvent.EndCapture) },
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier
					.weight(1f)
					.height(40.dp),
				contentPadding = PaddingValues(horizontal = DimensSmall100),
				enabled = uiState.capturingActive && uiState.wifiConnected,
			) {
				Text(
					text = stringResource(id = R.string.photo_control_end_capture).uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
			}
		}
	}
}

@Composable
private fun CaptureInfo(uiState: DashboardUiState) {
	// TODO not easy to accurately represent, tweak later
//	Row(
//		Modifier
//			.fillMaxWidth()
//			.padding(horizontal = DimensNormal100),
//		horizontalArrangement = Arrangement.SpaceBetween,
//		verticalAlignment = Alignment.CenterVertically
//	) {
//		Text(
//			text = stringResource(id = R.string.photo_control_exposure_count).uppercase(),
//			style = textStyle12Bold,
//			color = AppTheme.colorScheme.secondary
//		)
//		Text(
//			text = uiState.getCaptureRatio(),
//			style = textStyle16Bold,
//			color = AppTheme.colorScheme.secondary
//		)
//	}

	Row(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = DimensNormal100)
			.padding(top = DimensSmall100),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = stringResource(id = R.string.photo_control_start_time).uppercase(),
			style = textStyle12Bold,
			color = AppTheme.colorScheme.secondary
		)

		val unixTime = Instant.ofEpochMilli(uiState.captureStartTime ?: 0)
			.atZone(ZoneId.systemDefault())

		Text(
			text = buildString {
				append(unixTime.hour)
				append(":")
				append(formatter.format(unixTime.minute))
				append(":")
				append(formatter.format(unixTime.second))
			},
			style = textStyle16Bold,
			color = AppTheme.colorScheme.secondary
		)
	}

	Row(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = DimensNormal100)
			.padding(top = DimensSmall100),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = stringResource(id = R.string.photo_control_estimated_time).uppercase(),
			style = textStyle12Bold,
			color = AppTheme.colorScheme.secondary
		)
		Text(
			text = buildString {
				append(uiState.captureEstimatedTimeMillis?.toHours() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_hour))
				append(" ")
				append(uiState.captureEstimatedTimeMillis?.toMinutes() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_minute))
				append(" ")
				append(uiState.captureEstimatedTimeMillis?.toSeconds() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_second))
			},
			style = textStyle16Bold,
			color = AppTheme.colorScheme.secondary
		)
	}

	Row(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = DimensNormal100)
			.padding(top = DimensSmall100),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = stringResource(id = R.string.photo_control_elapsed_time).uppercase(),
			style = textStyle12Bold,
			color = AppTheme.colorScheme.secondary
		)
		Text(
			text = buildString {
				append(uiState.captureElapsedTimeMillis?.toHours() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_hour))
				append(" ")
				append(uiState.captureElapsedTimeMillis?.toMinutes() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_minute))
				append(" ")
				append(uiState.captureElapsedTimeMillis?.toSeconds() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_second))
			},
			style = textStyle16Bold,
			color = AppTheme.colorScheme.secondary
		)
	}

	Divider(
		modifier = Modifier.padding(vertical = DimensNormal100)
	)
}

@Preview
@Composable
fun PhotoControlCardPreview() {
	AppTheme {
		PhotoControlCard(
			uiState = DashboardUiState(),
			onPhotoControlEvent = {},
			notifyAboutChange = { _, _ -> }
		)
	}
}
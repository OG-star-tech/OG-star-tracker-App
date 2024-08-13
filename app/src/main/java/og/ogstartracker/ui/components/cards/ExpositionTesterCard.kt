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
import og.ogstartracker.domain.events.ExpositionTesterEvent
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.components.common.input.ActionInput
import og.ogstartracker.ui.screens.DashboardUiState
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensSmall100
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

@Composable
fun ExpositionTesterCard(
	uiState: DashboardUiState,
	onExpositionTesterEvent: (ExpositionTesterEvent) -> Unit,
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
			.segmentedShadow(color.takeIf { uiState.exposureTestingActive } ?: AppTheme.colorScheme.shadow)
			.padding(DimensNormal100)
			.clip(ShapeNormal)
			.background(color = ColorBackground)
			.animateContentSize()
			.alpha(Constants.Percent.PERCENT_100.takeIf { uiState.wifiConnected && !uiState.capturingActive } ?: Constants.Percent.PERCENT_50)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_exposition_check),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.exposure_test_title).uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = stringResource(id = R.string.exposure_test_subtitle),
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}
		}

		Text(
			text = stringResource(id = R.string.exposure_test_info),
			style = textStyle12Bold,
			color = AppTheme.colorScheme.secondary,
			modifier = Modifier.padding(horizontal = DimensNormal100)
		)

		Column(
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensNormal100)
		) {
			ActionInput(
				enabled = !uiState.exposureTestingActive && uiState.wifiConnected,
				textFieldState = uiState.expositionTesterStart,
				label = stringResource(id = R.string.exposure_test_start_time),
				placeholder = "0",
				modifier = Modifier.padding(top = DimensSmall100),
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_sec),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.expositionTesterStart.setNewState(it)
				}
			)
			ActionInput(
				enabled = !uiState.exposureTestingActive && uiState.wifiConnected,
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.expositionTesterEnd,
				label = stringResource(id = R.string.exposure_test_end_time),
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				onValueChange = {
					uiState.expositionTesterEnd.setNewState(it)
				},
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_sec),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
			)
			ActionInput(
				enabled = !uiState.exposureTestingActive && uiState.wifiConnected,
				textFieldState = uiState.expositionTesterStepSize,
				modifier = Modifier.padding(top = DimensSmall100),
				label = stringResource(id = R.string.exposure_test_step_size),
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_sec),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.expositionTesterStepSize.setNewState(it)
				}
			)
			ActionInput(
				enabled = !uiState.exposureTestingActive && uiState.wifiConnected,
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.expositionTesterDelay,
				label = stringResource(id = R.string.exposure_test_delay_time),
				placeholder = "0",
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Decimal,
				trailingIcon = {
					Text(
						stringResource(id = R.string.photo_control_sec),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				},
				onValueChange = {
					uiState.expositionTesterDelay.setNewState(it)
				}
			)
		}

		if (uiState.exposureTestingActive) {
			Divider(modifier = Modifier.padding(top = DimensNormal100))
			ExposureTesterInfo(estimatedTime = uiState.expositionTesterTimeEstimateMillis)
		}

		Row(
			horizontalArrangement = Arrangement.spacedBy(DimensNormal100),
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(vertical = DimensNormal100)
		) {
			Button(
				onClick = {
					onExpositionTesterEvent(ExpositionTesterEvent.StartTest)
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
				enabled = uiState.areExpositionInputsValid() && !uiState.capturingActive && uiState.wifiConnected && !uiState.exposureTestingActive,
			) {
				Text(
					text = stringResource(id = R.string.exposure_test_start).uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
			}

			Button(
				onClick = { onExpositionTesterEvent(ExpositionTesterEvent.EndTest) },
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
				enabled = uiState.areExpositionInputsValid() && !uiState.capturingActive && uiState.wifiConnected && uiState.exposureTestingActive,
			) {
				Text(
					text = stringResource(id = R.string.exposure_test_end).uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
			}
		}
	}
}

@Composable
private fun ExposureTesterInfo(estimatedTime: Long?) {
	Row(
		Modifier
			.fillMaxWidth()
			.padding(horizontal = DimensNormal100)
			.padding(top = DimensNormal100),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = stringResource(id = R.string.exposure_test_estimate).uppercase(),
			style = textStyle12Bold,
			color = AppTheme.colorScheme.secondary
		)
		Text(
			text = buildString {
				append(estimatedTime?.toHours() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_hour))
				append(" ")
				append(estimatedTime?.toMinutes() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_minute))
				append(" ")
				append(estimatedTime?.toSeconds() ?: 0)
				append(stringResource(id = R.string.photo_control_elapsed_time_second))
			},
			style = textStyle16Bold,
			color = AppTheme.colorScheme.secondary
		)
	}

	Divider(
		modifier = Modifier.padding(top = DimensNormal100)
	)
}

@Preview
@Composable
fun ExpositionTesterCardPreview() {
	AppTheme {
		ExpositionTesterCard(
			uiState = DashboardUiState(),
			onExpositionTesterEvent = {},
		)
	}
}
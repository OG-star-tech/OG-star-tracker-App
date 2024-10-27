package og.ogstartracker.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import me.zhanghai.compose.preference.ListPreferenceType
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preference
import me.zhanghai.compose.preference.preferenceCategory
import og.ogstartracker.Config.PREFERENCES_HEMISPHERE
import og.ogstartracker.Config.PREFERENCES_TRACKING_MODE
import og.ogstartracker.Config.PREFERENCES_VIBRATIONS
import og.ogstartracker.R
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.models.TrackingMode
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.textStyle14Bold
import og.ogstartracker.ui.theme.textStyle16Regular
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.SystemUiHelper
import og.ogstartracker.utils.switchPreferences
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
	navController: NavController,
	viewModel: SettingsViewModel = koinViewModel(),
) {
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	val uiState by viewModel.uiState.collectAsState()

	SettingsScreenContent(
		uiState = uiState,
		onBack = navController::navigateUp,
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
	uiState: SettingsUiState,
	onBack: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.settings_title),
						style = textStyle20Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				navigationIcon = {
					IconButton(
						onClick = onBack
					) {
						Icon(
							imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
							tint = AppTheme.colorScheme.primary,
							contentDescription = null
						)
					}
				}
			)
		},
		content = { paddings ->
			SettingsScreenLayout(
				modifier = Modifier.padding(top = paddings.calculateTopPadding()),
				uiState = uiState,
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun SettingsScreenLayout(
	uiState: SettingsUiState,
	modifier: Modifier = Modifier,
) {
	val context = LocalContext.current

	ProvidePreferenceLocals {
		LazyColumn(modifier = modifier.fillMaxSize()) {
			preferenceCategory(
				key = "general",
				title = {
					Text(
						text = stringResource(id = R.string.settings_general),
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				}
			)

			listPreference(
				key = PREFERENCES_HEMISPHERE,
				icon = {
					Icon(
						imageVector = ImageVector.vectorResource(id = R.drawable.ic_earth),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null
					)
				},
				defaultValue = context.getString(Hemisphere.NORTH.text),
				values = Hemisphere.entries.map { context.getString(it.text) },
				title = {
					Text(
						text = stringResource(id = R.string.settings_hemisphere),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.primary
					)
				},
				summary = {
					Text(
						text = it,
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				type = ListPreferenceType.ALERT_DIALOG
			)

			listPreference(
				key = PREFERENCES_TRACKING_MODE,
				icon = {
					Icon(
						imageVector = ImageVector.vectorResource(id = R.drawable.ic_orbit),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null
					)
				},
				defaultValue = context.getString(TrackingMode.SIDEREAL.text),
				values = TrackingMode.entries.map { context.getString(it.text) },
				title = {
					Text(
						text = stringResource(id = R.string.settings_tracking_mode),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.primary
					)
				},
				summary = {
					Text(
						text = it,
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				type = ListPreferenceType.ALERT_DIALOG,
			)

			switchPreferences(
				key = PREFERENCES_VIBRATIONS,
				defaultValue = true,
				title = {
					Text(
						text = stringResource(id = R.string.settings_vibrations),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.primary
					)
				},
				icon = {
					Icon(
						imageVector = ImageVector.vectorResource(id = R.drawable.vibrate),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier.padding(end = DimensNormal100)
					)
				}
			)

			preferenceCategory(
				key = "version_details",
				title = {
					Text(
						text = stringResource(id = R.string.settings_version_info),
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				}
			)

			preference(
				key = "tracker_version",
				title = {
					Text(
						text = stringResource(id = R.string.settings_tracker_firmware),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.primary
					)
				},
				summary = {
					Text(
						text = uiState.version?.toString() ?: stringResource(id = R.string.settings_tracker_firmware_cannot_detect),
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				icon = {
					Icon(
						imageVector = ImageVector.vectorResource(id = R.drawable.ic_cog),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier.padding(end = DimensNormal100)
					)
				},
				enabled = false,
			)

			preference(
				key = "app_version",
				title = {
					Text(
						text = stringResource(id = R.string.settings_android_version),
						style = textStyle16Regular,
						color = AppTheme.colorScheme.primary
					)
				},
				summary = {
					Text(
						text = context.packageManager.getPackageInfo(context.packageName, 0).versionName,
						style = textStyle14Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				icon = {
					Icon(
						imageVector = ImageVector.vectorResource(id = R.drawable.android),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier.padding(end = DimensNormal100)
					)
				},
				enabled = false,
			)
		}
	}
}

@Composable
@Preview
internal fun SettingContentPreview() {
	AppTheme {
		SettingsScreenContent(
			uiState = SettingsUiState(),
			onBack = {},
		)
	}
}

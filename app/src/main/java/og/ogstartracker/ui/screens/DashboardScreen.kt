package og.ogstartracker.ui.screens

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_WIFI_SETTINGS
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import og.ogstartracker.Config
import og.ogstartracker.Config.SCREEN_SETTINGS
import og.ogstartracker.R
import og.ogstartracker.domain.events.ExpositionTesterEvent
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.models.CheckListItem
import og.ogstartracker.domain.usecases.settings.SettingItem
import og.ogstartracker.ui.components.InfoDialog
import og.ogstartracker.ui.components.cards.ChecklistCard
import og.ogstartracker.ui.components.cards.ConnectionCard
import og.ogstartracker.ui.components.cards.ExpositionTesterCard
import og.ogstartracker.ui.components.cards.PhotoControlCard
import og.ogstartracker.ui.components.cards.SiderealCard
import og.ogstartracker.ui.components.cards.SlewControlCard
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.components.common.LocalInsets
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.BigGeneralIconSize
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.DimensNormal75
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.DimensSmall50
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.SystemUiHelper
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
	navController: NavController,
	viewModel: DashboardViewModel = koinViewModel(),
) {
	// setup system icon colors
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	val context = LocalContext.current

	val uiState by viewModel.uiState.collectAsState()

	val fineLocationPermissionState = rememberPermissionState(
		android.Manifest.permission.ACCESS_FINE_LOCATION
	)

	val checkWifi by viewModel.checkWifiEvent.collectAsState()
	if (checkWifi) {
		checkLocationPermission(viewModel, fineLocationPermissionState, context)

		viewModel.resetWifiEvent()
	}

	InitLifecycleListener(
		onResume = {
			viewModel.startWiFiTimer()
			checkLocationPermission(viewModel, fineLocationPermissionState, context)
		},
		onStop = viewModel::stopWiFiTimer
	)

	LaunchedEffect(uiState.lastMessage) {
		uiState.lastMessage?.let { message ->
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
			viewModel.resetMessage()
		}
	}

	var showInfoDialog by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	if (uiState.shouldShowOnboardingDialog) {
		showInfoDialog = true
	}

	LaunchedEffect(Unit) {
		// preload last user settings
		scope.launch {
			viewModel.settingsItemsFlow.first()
		}
	}

	DashboardScreenContent(
		uiState = uiState,
		onChecklistClicked = viewModel::changeChecklist,
		onSiderealClicked = viewModel::changeSidereal,
		onSlewControlEvent = viewModel::slewControlEvent,
		onPhotoControlEvent = viewModel::photoControlEvent,
		onGearClick = {
			navController.navigate(SCREEN_SETTINGS)
		},
		onInfoClick = {
			showInfoDialog = true
		},
		notifyAboutChange = viewModel::notifyCacheAboutChange,
		onConnectionClick = {
			if (uiState.wifiConnected) return@DashboardScreenContent

			when {
				// user permanently banned location, navigate to app settings
				fineLocationPermissionState.status == PermissionStatus.Denied(shouldShowRationale = true) ->
					context.startActivity(Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
						setData(Uri.fromParts("package", context.packageName, null))
					})

				// user did not enabled location, request
				!fineLocationPermissionState.status.isGranted && !fineLocationPermissionState.status.shouldShowRationale -> {
					fineLocationPermissionState.launchPermissionRequest()
				}

				// user enabled location, but is on wrong wifi, open settings
				fineLocationPermissionState.status.isGranted -> {
					context.startActivity(Intent(ACTION_WIFI_SETTINGS))
				}
			}
		},
		onChecklistItemClicked = viewModel::updateCheckListItem,
		onExpositionTesterEvent = viewModel::expositionTesterEvent
	)

	if (showInfoDialog) {
		InfoDialog(onHide = {
			viewModel.setUserSawOnboard()
			showInfoDialog = false
		})
	}
}

@OptIn(ExperimentalPermissionsApi::class)
private fun checkLocationPermission(
	viewModel: DashboardViewModel,
	fineLocationPermissionState: PermissionState,
	context: Context
) {
	viewModel.setHaveLocationPermission(fineLocationPermissionState.status.isGranted)

	fineLocationPermissionState.status.isGranted.let {
		// detect if user is on the correct wifi
		if (checkWifiConnection(context, viewModel)) return@let
	}
}

private fun checkWifiConnection(
	context: Context,
	viewModel: DashboardViewModel
): Boolean {
	val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return true
	val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

	networkCapabilities?.takeIf { it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) }?.let innerLet@{
		val wifiManager = context.getSystemService<WifiManager>() ?: return false

		val correctWifi = wifiManager.connectionInfo.ssid == Config.WIFI_SSID
		viewModel.setConnection(correctWifi)
	}
	return false
}

@Composable
private fun DashboardScreenContent(
	uiState: DashboardUiState,
	onChecklistClicked: () -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onChecklistItemClicked: (CheckListItem) -> Unit,
	onExpositionTesterEvent: (ExpositionTesterEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	onGearClick: () -> Unit,
	onConnectionClick: () -> Unit,
	notifyAboutChange: (SettingItem, Int?) -> Unit,
	onInfoClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		content = { paddings ->
			DashboardScreenLayout(
				modifier = Modifier.padding(top = paddings.calculateTopPadding()),
				uiState = uiState,
				onChecklistClicked = onChecklistClicked,
				onSiderealClicked = onSiderealClicked,
				onSlewControlEvent = onSlewControlEvent,
				onPhotoControlEvent = onPhotoControlEvent,
				onGearClick = onGearClick,
				onInfoClick = onInfoClick,
				notifyAboutChange = notifyAboutChange,
				onConnectionClick = onConnectionClick,
				onChecklistItemClicked = onChecklistItemClicked,
				onExpositionTesterEvent = onExpositionTesterEvent,
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun DashboardScreenLayout(
	uiState: DashboardUiState,
	onChecklistClicked: () -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	onExpositionTesterEvent: (ExpositionTesterEvent) -> Unit,
	onChecklistItemClicked: (CheckListItem) -> Unit,
	onGearClick: () -> Unit,
	onConnectionClick: () -> Unit,
	notifyAboutChange: (SettingItem, Int?) -> Unit,
	onInfoClick: () -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val state = rememberLazyListState()

	LazyColumn(
		state = state,
		modifier = modifier,
		contentPadding = PaddingValues(
			top = DimensNormal100,
			bottom = LocalInsets.current.navigationBarInset + DimensNormal200
		),
		verticalArrangement = Arrangement.spacedBy(DimensSmall50)
	) {
		item {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				IconButton(onClick = { onInfoClick() }) {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_information),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier
							.size(BigGeneralIconSize)
							.padding(DimensSmall100)
					)
				}

				Text(
					modifier = Modifier
						.weight(1f)
						.padding(vertical = DimensSmall100),
					text = stringResource(id = R.string.main_title).uppercase(),
					style = textStyle20Bold,
					textAlign = TextAlign.Center,
					color = AppTheme.colorScheme.primary,
				)

				IconButton(onClick = { onGearClick() }) {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_cog),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier
							.size(BigGeneralIconSize)
							.padding(DimensSmall100)
					)
				}
			}
		}

		item {
			ConnectionCard(
				connected = uiState.wifiConnected,
				onCardClick = onConnectionClick,
				haveLocationPermission = uiState.haveLocationPermission,
			)
		}

		item {
			Divider(modifier = Modifier.padding(vertical = DimensNormal75))
		}

		item {
			ChecklistCard(
				opened = uiState.openedCheckbox,
				onClick = onChecklistClicked,
				enabled = uiState.wifiConnected,
				checkListItems = uiState.checkListItems,
				onCardClick = onChecklistItemClicked,
			)
		}

		item {
			SiderealCard(
				active = uiState.siderealActive,
				onCheckChanged = {
					onSiderealClicked(!uiState.siderealActive)
				},
				enabled = uiState.wifiConnected,
			)
		}

		item {
			SlewControlCard(
				slewControlCommands = onSlewControlEvent,
				stepSize = uiState.slewValue,
				enabled = uiState.wifiConnected,
			)
		}

		item {
			PhotoControlCard(
				uiState = uiState,
				onPhotoControlEvent = onPhotoControlEvent,
				notifyAboutChange = notifyAboutChange,
			)
		}

		item {
			ExpositionTesterCard(
				uiState = uiState,
				onExpositionTesterEvent = onExpositionTesterEvent,
			)
		}
	}
}

@Composable
private fun InitLifecycleListener(onResume: suspend () -> Unit, onStop: suspend () -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			lifecycleOwner.lifecycleScope.launch {
				if (event == Lifecycle.Event.ON_STOP) {
					onStop()
				}

				if (event == Lifecycle.Event.ON_RESUME) {
					onResume()
				}
			}
		}

		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}
}

@Composable
@Preview
internal fun HomeScreenContentPreview() {
	AppTheme {
		DashboardScreenContent(
			uiState = DashboardUiState(),
			onChecklistClicked = {},
			onSiderealClicked = {},
			onSlewControlEvent = {},
			onPhotoControlEvent = {},
			onInfoClick = {},
			onGearClick = {},
			notifyAboutChange = { _, _ -> },
			onConnectionClick = {},
			onChecklistItemClicked = {},
			onExpositionTesterEvent = {}
		)
	}
}

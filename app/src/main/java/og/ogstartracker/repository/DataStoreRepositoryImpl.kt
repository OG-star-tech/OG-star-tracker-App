package og.ogstartracker.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import og.ogstartracker.domain.usecases.settings.SettingItem
import timber.log.Timber

class DataStoreRepositoryImpl constructor(
	private val context: Context
) : DataStoreRepository {

	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_DATA_STORE_NAME)

	override val exposureTime: Flow<Int?> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_EXPOSURE_TIME].takeIf { it != -1 } }

	override val exposureCount: Flow<Int?> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_EXPOSURE_COUNT].takeIf { it != -1 } }

	override val focalLength: Flow<Int?> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_FOCUS_LENGTH].takeIf { it != -1 } }

	override val pixelSize: Flow<Int?> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_PIXEL_SIZE].takeIf { it != -1 } }

	override val slewSpeed: Flow<Int?> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_SLEW_SPEED].takeIf { it != -1 } }

	override val ditherActive: Flow<Int> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_DITHER_ACTIVE] ?: 0 }

	override val stopTrackerActive: Flow<Int> = context.dataStore.data
		.map { preferences -> preferences[SETTINGS_STOP_TRACKER_ACTIVE] ?: 0 }

	override val userSawOnboarding: Flow<Boolean> = context.dataStore.data
		.map { preferences -> preferences[USER_SAW_ONBOARDING] ?: false }

	override suspend fun setNewSettings(settingItem: SettingItem, value: Int?) {
		Timber.d("setNewSettings, settingItem: $settingItem, value: $value")
		context.dataStore.edit { preferences ->
			when (settingItem) {
				SettingItem.SLEW_SPEED -> preferences[SETTINGS_SLEW_SPEED] = value ?: -1
				SettingItem.EXPOSURE_TIME -> preferences[SETTINGS_EXPOSURE_TIME] = value ?: -1
				SettingItem.EXPOSURE_COUNT -> preferences[SETTINGS_EXPOSURE_COUNT] = value ?: -1
				SettingItem.DITHER_ACTIVE -> preferences[SETTINGS_DITHER_ACTIVE] = value ?: 0
				SettingItem.FOCAL_LENGTH -> preferences[SETTINGS_FOCUS_LENGTH] = value ?: -1
				SettingItem.PIXEL_SIZE -> preferences[SETTINGS_PIXEL_SIZE] = value ?: -1
				SettingItem.STOP_TRACKING -> preferences[SETTINGS_STOP_TRACKER_ACTIVE] = value ?: 0
			}
		}
	}

	override suspend fun setUserSawOnboarding() {
		context.dataStore.edit { preferences ->
			preferences[USER_SAW_ONBOARDING] = true
		}
	}

	companion object {
		private const val PREFS_DATA_STORE_NAME = "store"
		private val SETTINGS_SLEW_SPEED = intPreferencesKey("slew_speed")
		private val SETTINGS_EXPOSURE_TIME = intPreferencesKey("exposure_time")
		private val SETTINGS_EXPOSURE_COUNT = intPreferencesKey("exposure_count")
		private val SETTINGS_FOCUS_LENGTH = intPreferencesKey("focus_length")
		private val SETTINGS_PIXEL_SIZE = intPreferencesKey("pixel_size")
		private val SETTINGS_DITHER_ACTIVE = intPreferencesKey("dither_active")
		private val SETTINGS_STOP_TRACKER_ACTIVE = intPreferencesKey("stop_tracking_active")
		private val USER_SAW_ONBOARDING = booleanPreferencesKey("onboarding")
	}
}
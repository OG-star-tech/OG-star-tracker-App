package og.ogstartracker.repository

import kotlinx.coroutines.flow.Flow
import og.ogstartracker.domain.usecases.settings.SettingItem

interface DataStoreRepository {

	/**
	 * The exposure time currently set in the settings.
	 *
	 * This is a Flow that emits the current exposure time set in the settings. The exposure time is in seconds.
	 */
	val exposureTime: Flow<Int?>

	/**
	 * The exposure count currently set in the settings.
	 *
	 * This is a Flow that emits the current exposure count set in the settings. The exposure count is the number of exposures to take.
	 */
	val exposureCount: Flow<Int?>

	/**
	 * The focal length currently set in the settings.
	 *
	 * This is a Flow that emits the current focal length set in the settings. The focal length is in millimeters.
	 */
	val focalLength: Flow<Int?>

	/**
	 * The pixel size currently set in the settings.
	 *
	 * This is a Flow that emits the current pixel size set in the settings. The pixel size is in micrometers.
	 */
	val pixelSize: Flow<Int?>

	/**
	 * The slew speed currently set in the settings.
	 *
	 * This is a Flow that emits the current slew speed set in the settings. The slew speed is the speed at which the telescope moves.
	 */
	val slewSpeed: Flow<Int?>

	/**
	 * The dither active status currently set in the settings.
	 *
	 * This is a Flow that emits the current dither active status set in the settings. If dithering is active, the value is 1. If not, the value is 0.
	 */
	val ditherActive: Flow<Int>

	/**
	 * The status of whether the user has seen the onboarding.
	 *
	 * This is a Flow that emits the current status of whether the user has seen the onboarding. If the user has seen the onboarding, the value is true. If not, the value is false.
	 */
	val userSawOnboarding: Flow<Boolean>

	/**
	 * Sets new settings.
	 *
	 * This function sets new settings. The setting to change and the new value are passed as parameters.
	 *
	 * @param settingItem The setting to change. This is a SettingItem object.
	 * @param value The new value to set for the setting. This can be any integer value.
	 */
	suspend fun setNewSettings(settingItem: SettingItem, value: Int?)

	/**
	 * Sets the status of whether the user has seen the onboarding to true.
	 *
	 * This function sets the status of whether the user has seen the onboarding to true. It does not take any parameters.
	 */
	suspend fun setUserSawOnboarding()
}
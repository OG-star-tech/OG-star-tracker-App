package og.ogstartracker.domain.usecases.arduino

import android.app.Activity
import android.content.Context
import og.ogstartracker.Config
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.models.TrackingMode
import og.ogstartracker.domain.usecases.base.ResourceSuspendProviderUseCase
import og.ogstartracker.network.Resource
import og.ogstartracker.repository.ArduinoRepository

private const val PREFERENCE_SUFFIX = "_preferences"

class StartSiderealTrackingUseCase constructor(
	private val repository: ArduinoRepository,
	private val context: Context,
) : ResourceSuspendProviderUseCase<String> {

	override suspend fun invoke(): Resource<String> {
		val preferences = context.getSharedPreferences(context.packageName + PREFERENCE_SUFFIX, Activity.MODE_PRIVATE)

		val hemisphere = preferences.getString(Config.PREFERENCES_HEMISPHERE, null)?.let { value ->
			Hemisphere.entries.firstOrNull { context.getString(it.text) == value }
		} ?: Hemisphere.NORTH

		val trackingModel = preferences.getString(Config.PREFERENCES_TRACKING_MODE, null)?.let { value ->
			TrackingMode.entries.firstOrNull { context.getString(it.text) == value }
		} ?: TrackingMode.SIDEREAL


		return repository.startSideRealTracking(hemisphere, trackingModel)
	}
}
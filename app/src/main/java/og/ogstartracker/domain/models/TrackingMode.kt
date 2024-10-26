package og.ogstartracker.domain.models

import androidx.annotation.StringRes
import og.ogstartracker.R

enum class TrackingMode constructor(
	val arduinoValue: Int,
	@StringRes val text: Int
) {
	SIDEREAL(0, R.string.settings_tracking_mode_sidereal),
	SOLAR(1, R.string.settings_tracking_mode_solar),
	LUNAR(2, R.string.settings_tracking_mode_lunar),
}
package og.ogstartracker

object Config {
	const val SLEW_MIN_VALUE = 0
	const val SLEW_MAX_VALUE = 5

	const val WIFI_SSID = "\"OG Star Tracker\""
	const val WIFI_SSID_UNKNOWN = "<unknown ssid>" // returned when the app is not running

	const val STATUS_TRACKING_ON = "Tracking ON"
	const val STATUS_IDLE = "Idle"
	const val STATUS_CAPTURING = "Captures Remaining"

	const val CAPTURING_INITIAL_DELAY = 3000L
	const val CHECK_WIFI_DURATION = 5_000L

	const val SCREEN_DASHBOARD = "dashboard"
	const val SCREEN_SETTINGS = "settings"

	const val PREFERENCES_VIBRATIONS = "vibrations"
	const val PREFERENCES_HEMISPHERE = "hemisphere"
	const val PREFERENCES_TRACKING_MODE = "tracking_mode"
}
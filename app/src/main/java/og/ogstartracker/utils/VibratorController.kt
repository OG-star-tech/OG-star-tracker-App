package og.ogstartracker.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import og.ogstartracker.Config.PREFERENCES_VIBRATIONS
import og.ogstartracker.domain.usecases.arduino.PREFERENCE_SUFFIX

/**
 * Controller class for vibrator. Handles multiple Android SDK versions at a time.
 *
 * @property repeat The index into the timings array at which to repeat, or -1 if you don't want to repeat indefinitely.
 * @property vibrationPattern allows to define which vibration pattern should be used for vibrations. It is used to
 * create VibrationEffect on Android SDK O+ devices. On older devices it is used directly.
 */
class VibratorController constructor(
	private val context: Context,
) {
	private var vibrator: Vibrator? = null
	private var vibratorManager: VibratorManager? = null

	private fun areVibrationsEnabled(): Boolean {
		val preferences = context.getSharedPreferences(context.packageName + PREFERENCE_SUFFIX, Activity.MODE_PRIVATE)
		return preferences.getBoolean(PREFERENCES_VIBRATIONS, true)
	}

	/**
	 * Starts vibrations based on the SDK version. If the device is S+ it will use [startVibrationsOnS] to start it else
	 * it will use [startVibrationsPreS].
	 */
	fun startVibrations(vibrationPattern: LongArray) {
		if (!areVibrationsEnabled()) return

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			startVibrationsOnS(vibrationPattern)
		} else {
			startVibrationsPreS(vibrationPattern)
		}
	}

	/**
	 * Stops vibrations based on the SDK version. If the device is S+ it will use [stopVibrationOnS] to stop it else it
	 * will use [stopVibrationsPreS].
	 *
	 * @param context is used to initialize [vibratorManager] or [vibrator] based on the SDK version
	 */
	@Suppress("unused")
	fun stopVibrations() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			stopVibrationOnS()
		} else {
			stopVibrationsPreS()
		}
	}

	/**
	 * Starts vibration on S+ devices. Initialized [vibratorManager] when needed (using context) and it start a
	 * [vibration] if the HW has a vibrator. If not then nothing happens.
	 *
	 * @param context is used to initialize [vibratorManager]
	 */
	@SuppressLint("MissingPermission")
	@RequiresApi(Build.VERSION_CODES.S)
	private fun startVibrationsOnS(vibrationPattern: LongArray) {
		if (vibratorManager == null) {
			vibratorManager = context.getSystemService()
		}

		vibratorManager?.defaultVibrator?.apply {
			if (hasVibrator()) {
				vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
			}
		}
	}

	/**
	 * Starts vibration on pre S devices. Initialized [vibrator] when needed (using context) and it start a [vibration]
	 * if the HW has a vibrator. If not then nothing happens. Uses [VibrationEffect] for devices with Android SDK O+ and
	 * [vibrationPattern] for pre O devices.
	 *
	 * @param context is used to initialize [vibrator]
	 */
	@SuppressLint("MissingPermission")
	private fun startVibrationsPreS(vibrationPattern: LongArray) {
		if (vibrator == null) {
			vibrator = context.getSystemService()
		}

		when {
			vibrator?.hasVibrator() == false -> Unit
			else -> vibrator?.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
		}
	}

	/**
	 * Stops vibration on S+ devices. Initialized [vibratorManager] when needed (using context) and it cancels a
	 * [vibration].
	 *
	 * @param context is used to initialize [vibratorManager]
	 */
	@SuppressLint("MissingPermission")
	@RequiresApi(Build.VERSION_CODES.S)
	private fun stopVibrationOnS() {
		if (vibratorManager == null) {
			vibratorManager = context.getSystemService()
		}
		vibratorManager?.cancel()
	}

	/**
	 * Stops vibration on pre S devices. Initialized [vibrator] when needed (using context) and it cancels a
	 * [vibration].
	 *
	 * @param context is used to initialize [vibrator]
	 */
	@SuppressLint("MissingPermission")
	private fun stopVibrationsPreS() {
		if (vibrator == null) {
			vibrator = context.getSystemService()
		}
		vibrator?.cancel()
	}
}

val vibrationPatternThreeClick: LongArray = longArrayOf(0, 50)
val vibrationPatternClick: LongArray = longArrayOf(0, 50)

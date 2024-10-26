package og.ogstartracker.domain.usecases.providers

import og.ogstartracker.domain.usecases.arduino.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.arduino.GetCurrentStateUseCase
import og.ogstartracker.domain.usecases.arduino.GetLastArduinoMessageUseCase
import og.ogstartracker.domain.usecases.arduino.GetVersionUseCase
import og.ogstartracker.domain.usecases.arduino.ResetLastArduinoMessageUseCase
import og.ogstartracker.domain.usecases.arduino.StartCaptureUseCase
import og.ogstartracker.domain.usecases.arduino.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerRightUseCase
import og.ogstartracker.domain.usecases.onboarding.DidUserSeeOnboardingUseCase
import og.ogstartracker.domain.usecases.onboarding.SetUserSawOnboardingUseCase
import og.ogstartracker.domain.usecases.settings.GetSettingsUseCase
import og.ogstartracker.domain.usecases.settings.SetNewSettingsUseCase

data class DashboardUseCaseProvider constructor(
	val startSiderealTracking: StartSiderealTrackingUseCase,
	val stopSiderealTracking: StopSiderealTrackingUseCase,
	val trackerLeft: TurnTrackerLeftUseCase,
	val trackerRight: TurnTrackerRightUseCase,
	val startCapture: StartCaptureUseCase,
	val abortCapture: AbortCaptureUseCase,
	val setNewSettings: SetNewSettingsUseCase,
	val setUserSawOnboarding: SetUserSawOnboardingUseCase,
	val resetLastArduinoMessage: ResetLastArduinoMessageUseCase,
	val getLastArduinoMessage: GetLastArduinoMessageUseCase,
	val didUserSeeOnboarding: DidUserSeeOnboardingUseCase,
	val getSettings: GetSettingsUseCase,
	val getCurrentState: GetCurrentStateUseCase,
	val getVersion: GetVersionUseCase,
)
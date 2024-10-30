package og.ogstartracker.di

import og.ogstartracker.domain.usecases.arduino.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.arduino.GetCurrentStateUseCase
import og.ogstartracker.domain.usecases.arduino.GetLastArduinoMessageUseCase
import og.ogstartracker.domain.usecases.arduino.GetVersionUseCase
import og.ogstartracker.domain.usecases.arduino.ResetLastArduinoMessageUseCase
import og.ogstartracker.domain.usecases.arduino.StartCaptureUseCase
import og.ogstartracker.domain.usecases.arduino.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.StopTrackerSlewUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerRightUseCase
import og.ogstartracker.domain.usecases.onboarding.DidUserSeeOnboardingUseCase
import og.ogstartracker.domain.usecases.onboarding.SetUserSawOnboardingUseCase
import og.ogstartracker.domain.usecases.providers.DashboardUseCaseProvider
import og.ogstartracker.domain.usecases.settings.GetSettingsUseCase
import og.ogstartracker.domain.usecases.settings.SetNewSettingsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val useCaseModule = module {

	single {
		StartSiderealTrackingUseCase(
			repository = get(),
			context = androidContext(),
		)
	}

	single {
		StopSiderealTrackingUseCase(
			repository = get()
		)
	}

	single {
		StopTrackerSlewUseCase(
			repository = get()
		)
	}

	single {
		TurnTrackerLeftUseCase(
			repository = get()
		)
	}

	single {
		TurnTrackerRightUseCase(
			repository = get()
		)
	}

	single {
		StartCaptureUseCase(
			repository = get()
		)
	}

	single {
		AbortCaptureUseCase(
			repository = get()
		)
	}

	single {
		GetCurrentStateUseCase(
			repository = get()
		)
	}

	single {
		GetSettingsUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		SetNewSettingsUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		DidUserSeeOnboardingUseCase(
			repository = get()
		)
	}

	single {
		SetUserSawOnboardingUseCase(
			repository = get()
		)
	}

	single {
		GetLastArduinoMessageUseCase(
			repository = get()
		)
	}

	single {
		ResetLastArduinoMessageUseCase(
			repository = get()
		)
	}

	single {
		GetVersionUseCase(
			repository = get()
		)
	}

	single {
		DashboardUseCaseProvider(
			startCapture = get(),
			resetLastArduinoMessage = get(),
			getSettings = get(),
			getLastArduinoMessage = get(),
			didUserSeeOnboarding = get(),
			setUserSawOnboarding = get(),
			abortCapture = get(),
			setNewSettings = get(),
			trackerRight = get(),
			stopSiderealTracking = get(),
			trackerLeft = get(),
			startSiderealTracking = get(),
			getCurrentState = get(),
			getVersion = get(),
			stopTrackerSlew = get(),
		)
	}
}
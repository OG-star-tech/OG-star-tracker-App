package og.ogstartracker.domain.usecases.settings

import og.ogstartracker.domain.usecases.base.SuspendUseCase
import og.ogstartracker.repository.DataStoreRepository

class SetNewSettingsUseCase constructor(
	private val dataStoreRepository: DataStoreRepository,
) : SuspendUseCase<SetNewSettingsUseCase.Input, Unit> {

	override suspend fun invoke(input: Input) {
		dataStoreRepository.setNewSettings(input.settingItem, input.value)
	}

	data class Input constructor(
		val settingItem: SettingItem,
		val value: Int?,
	)
}

enum class SettingItem {
	SLEW_SPEED,
	EXPOSURE_TIME,
	EXPOSURE_COUNT,
	DITHER_ACTIVE,
	FOCAL_LENGTH,
	PIXEL_SIZE,
	STOP_TRACKING
}
package og.ogstartracker.domain.usecases.settings

import kotlinx.coroutines.flow.Flow
import og.ogstartracker.domain.usecases.base.FlowUseCase
import og.ogstartracker.repository.DataStoreRepository

class GetSettingsUseCase constructor(
	private val dataStoreRepository: DataStoreRepository,
) : FlowUseCase<SettingItem, Int?> {

	override fun invoke(input: SettingItem): Flow<Int?> {
		return when (input) {
			SettingItem.SLEW_SPEED -> dataStoreRepository.slewSpeed
			SettingItem.EXPOSURE_TIME -> dataStoreRepository.exposureTime
			SettingItem.EXPOSURE_COUNT -> dataStoreRepository.exposureCount
			SettingItem.DITHER_ACTIVE -> dataStoreRepository.ditherActive
			SettingItem.FOCAL_LENGTH -> dataStoreRepository.focalLength
			SettingItem.PIXEL_SIZE -> dataStoreRepository.pixelSize
			SettingItem.STOP_TRACKING -> dataStoreRepository.stopTrackerActive
		}
	}
}
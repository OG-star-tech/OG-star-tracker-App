package og.ogstartracker.domain.events

sealed class PhotoControlEvent {

	data class DitheringActivation constructor(
		val active: Boolean
	) : PhotoControlEvent()

	data object StartCapture : PhotoControlEvent()

	data object EndCapture : PhotoControlEvent()

	data class StopTrackingActivation constructor(
		val active: Boolean
	) : PhotoControlEvent()
}
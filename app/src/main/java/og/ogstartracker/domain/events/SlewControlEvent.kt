package og.ogstartracker.domain.events

sealed class SlewControlEvent {

	data object RotateClockwise : SlewControlEvent()

	data object RotateAnticlockwise : SlewControlEvent()

	data object Release : SlewControlEvent()

	data class NewSpeed constructor(
		val speed: Int
	) : SlewControlEvent()

	data object TriggerVibration : SlewControlEvent()
}
package og.ogstartracker.domain.events

sealed class ExpositionTesterEvent {

	data object StartTest : ExpositionTesterEvent()

	data object EndTest : ExpositionTesterEvent()
}
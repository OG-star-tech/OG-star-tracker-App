package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.ResourceSuspendUseCase
import og.ogstartracker.repository.ArduinoRepository

class GetCurrentStateUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendUseCase<GetCurrentStateUseCase.Input, String> {

	override suspend fun invoke(input: Input) = repository.getStatus(input.showInUI)

	data class Input constructor(
		val showInUI: Boolean = true,
	)
}
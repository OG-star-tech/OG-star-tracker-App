package og.ogstartracker.network

sealed class ErrorIdentificationImpl constructor(
	override val code: Int = -1,
) : ErrorIdentification(code) {

	data object None : ErrorIdentificationImpl()

	class GeneralError constructor(
		code: Int? = null,
	) : ErrorIdentificationImpl(
		code = code ?: CODE_UNKNOWN,
	)

	data object ConnectionProblem : ErrorIdentificationImpl(
		code = CONNECTION_PROBLEM
	)

	data object Unknown : ErrorIdentificationImpl(
		code = CODE_UNKNOWN
	)

	data object NoContent : ErrorIdentificationImpl(
		code = CODE_NO_CONTENT
	)

	companion object {
		fun parseErrorCodes(
			code: Int,
		): ErrorIdentification {
			return when {
				code == 1 -> GeneralError(code)
				code == 2 -> GeneralError(code)
				else -> None
			}
		}
	}
}

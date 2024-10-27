package og.ogstartracker.network

open class ErrorIdentification constructor(
	open val code: Int = CODE_UNKNOWN,
) {
	companion object {
		const val CONNECTION_PROBLEM = 1
		const val CODE_UNKNOWN = -1
		const val CODE_NO_CONTENT = -2
	}
}

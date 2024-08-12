package og.ogstartracker.ui.components.common.input

class NotEmptyIntValidator : Validator {
	override fun isValid(text: String) = text.isNotBlank() && text.toIntOrNull() != null
}

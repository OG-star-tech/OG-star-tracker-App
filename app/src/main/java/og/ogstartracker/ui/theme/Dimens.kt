package og.ogstartracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Source https://medium.com/@june.pravin/dimensions-by-conventions-92b422e2216e
// Naming guidelines: https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md#do

val DimensSmall25 = 2.dp
val DimensSmall50 = 4.dp
val DimensSmall75 = 6.dp
val DimensSmall100 = 8.dp
val DimensSmall125 = 10.dp

val DimensNormal75 = 12.dp
val DimensNormal85 = 14.dp
val DimensNormal100 = 16.dp
val DimensNormal125 = 20.dp
val DimensNormal150 = 24.dp
val DimensNormal175 = 28.dp
val DimensNormal200 = 32.dp
val DimensNormal225 = 36.dp
val DimensNormal250 = 40.dp
val DimensNormal275 = 44.dp

val DimensLarge100 = 32.dp
val DimensLarge125 = 40.dp
val DimensLarge150 = 48.dp
val DimensLarge175 = 56.dp
val DimensLarge200 = 64.dp
val DimensLarge225 = 72.dp
val DimensLarge250 = 80.dp
val DimensLarge275 = 88.dp
val DimensLarge300 = 96.dp

val GeneralIconSize = 24.dp
val BigGeneralIconSize = 48.dp
val DropDownWidth = 100.dp
val SlewButtonHeight = 40.dp

val Shapes = Shapes(
	small = RoundedCornerShape(DimensSmall100),
	medium = RoundedCornerShape(DimensNormal75),
	large = RoundedCornerShape(DimensNormal100)
)

val ShapeNormal = RoundedCornerShape(DimensNormal100)
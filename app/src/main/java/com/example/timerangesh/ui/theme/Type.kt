package com.example.timerangesh.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.timerangesh.R

// 追加フォント
val NotoSerifJpBlackFont = FontFamily (
    Font(R.font.notoserifjp_black)
)

val NotoSerifJpBoldFont = FontFamily (
    Font(R.font.notoserifjp_bold)
)

val NotoSerifJpExtraBoldFont = FontFamily (
    Font(R.font.notoserifjp_extrabold)
)

val NotoSerifJpExtraLightFont = FontFamily (
    Font(R.font.notoserifjp_extralight)
)

val NotoSerifJpLightFont = FontFamily (
    Font(R.font.notoserifjp_light)
)

val NotoSerifJpMediumFont = FontFamily (
    Font(R.font.notoserifjp_medium)
)

val NotoSerifJpRegularFont = FontFamily (
    Font(R.font.notoserifjp_regular)
)

val NotoSerifJpSemiBoldFont = FontFamily (
    Font(R.font.notoserifjp_semibold)
)

val NotoSerifJpVariableFont = FontFamily (
    Font(R.font.notoserifjp_variablefont_wght)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)
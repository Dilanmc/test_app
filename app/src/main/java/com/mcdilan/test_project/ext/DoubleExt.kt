package com.mcdilan.test_project.ext

import com.mcdilan.test_project.R
import kotlin.math.roundToInt

fun Double.getColorForPCP(): Int {
    return when {
        (this < 0) -> {
            R.color.red
        }

        (this > 0) -> {
            R.color.green
        }

        else -> {
            R.color.grey
        }
    }
}

fun Double.roundToStep(step: Double): Double {
    return (this / step).roundToInt() * step
}
package com.aleferna.dotevolution.accessory

import javafx.scene.canvas.Canvas
import javafx.scene.control.TextInputControl
import javafx.scene.paint.Color
import tornadofx.*


// Extension function para facimente cobrir a canvas
fun Canvas.paint(color: Color) {
	graphicsContext2D.fill = color
	graphicsContext2D.fillRect(layoutX, layoutY, width, height)
}


// Shit that i shouldnt have to write myself...
fun Double.round(decimals: Int): Double {
	var multiplier = 1.0
	repeat(decimals) { multiplier *= 10 }
	return kotlin.math.round(this * multiplier) / multiplier
}


fun TextInputControl.limitDouble(lowerLimit: Number = Double.NEGATIVE_INFINITY, upperLimit: Number = Double.POSITIVE_INFINITY, trigger: ValidationTrigger = ValidationTrigger.OnChange()): TextInputControl {
	val message = "$lowerLimit <= float <= $upperLimit"
	validator(trigger) {
		if (!it.isNullOrEmpty()) {
			val parsed = it.replace(",", "")
			if (parsed.isDouble()) {
				if (parsed.toDouble() !in lowerLimit.toDouble()..upperLimit.toDouble()) {
					error(message)
				} else null
			} else error(message)
		} else error(message)
	}
	return this
}


fun TextInputControl.limitInt(lowerLimit: Number = Double.NEGATIVE_INFINITY, upperLimit: Number = Double.POSITIVE_INFINITY, trigger: ValidationTrigger = ValidationTrigger.OnChange()): TextInputControl {
	val message = "$lowerLimit <= integer <= $upperLimit"
	validator(trigger) {
		if (!it.isNullOrEmpty()) {
			val parsed = it.replace(",", "")
			if (parsed.isInt()) {
				if (parsed.toDouble() !in lowerLimit.toDouble()..upperLimit.toDouble()) {
					error(message)
				} else null
			} else error(message)
		} else error(message)
	}
	return this
}
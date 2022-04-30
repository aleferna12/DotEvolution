package com.aleferna.dotevolution.accessory

import com.aleferna.dotevolution.model.Parameters
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import org.apache.commons.math3.distribution.NormalDistribution
import tornadofx.*


// Classe para randomizar parâmetros baseado numa distribuição normal
open class EvolvableParameter(
	mean: Double = 0.0,
	mutRate: Double = 1.0,
	val lowerLimit: Double = Double.NEGATIVE_INFINITY,
	val upperLimit: Double = Double.POSITIVE_INFINITY
) {
	val meanProperty = SimpleDoubleProperty(mean)
	var mean by meanProperty
	val mutRateProperty = SimpleDoubleProperty(mutRate)
	var mutRate by mutRateProperty

	fun sample(): Double =
		if (mutRate != 0.0)
			NormalDistribution(mean, mutRate).sample().coerceIn(lowerLimit, upperLimit)
		else mean

	// This functions performs mutations for the values based on a general mutation rate
	// and relative mutation rate
	// Having a relative mutation rate helps the parameters to evolve in a similar rate
	fun normalMutate(mean: Double) =
		Evolution.normalMutate(mean, Parameters.mutationRate * mutRate).coerceIn(lowerLimit, upperLimit)
}


// Parametro randomizável para cores
open class ColorEvolvableParameter(
	color: Color,
	mutRate: Double = 1.0
) {
	val colorProperty = SimpleObjectProperty(color)
	var color: Color by colorProperty
	val mutRateProperty = SimpleDoubleProperty(mutRate)
	var mutRate by mutRateProperty

	fun sample(): Color =
		if (mutRate != 0.0)
			c(
				NormalDistribution(color.red, mutRate).sample().coerceIn(0.0, 1.0),
				NormalDistribution(color.green, mutRate).sample().coerceIn(0.0, 1.0),
				NormalDistribution(color.blue, mutRate).sample().coerceIn(0.0, 1.0)
			)
		else color
}

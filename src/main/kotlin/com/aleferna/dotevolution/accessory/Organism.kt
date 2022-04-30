package com.aleferna.dotevolution.accessory

import javafx.scene.paint.Color
import org.apache.commons.math3.distribution.NormalDistribution
import kotlin.math.cos
import kotlin.math.sin


object Evolution {
	fun incompleteDominance(val1: Double, val2: Double) = (val1 + val2) / 2

	fun normalMutate(mean: Double, mutRate: Double) =
		if (mutRate != 0.0) { NormalDistribution(mean, mutRate).sample() } else mean
}


// Organism that hold ecological and genetic data
open class Organism(
	var x: Double,
	var y: Double,
	var direction: Double,
	val trajectory: Double,
	invertTrajectory: Boolean,
	val speed: Double,
	val size: Double,
	val color: Color,
	var food: Double,
	val parentalInvestment: Double
	) {
	var speedX = cos(direction) * speed
	var speedY = sin(direction) * speed
	var age = 0.0
	val way = if (invertTrajectory) { -1 } else 1

	fun update(delta: Double, sizeCost: Double, speedCost: Double) {
		// Retira tempo de vida do organismo baseado em diversos fatores
		food -= delta + delta * (speed*speedCost + size*sizeCost)
		direction += trajectory * way * delta
		speedX = cos(direction) * speed
		speedY = sin(direction) * speed
		x += speedX * delta
		y += speedY * delta
		age += delta
	}

	fun collideCircle(circleX: Double, circleY: Double, circleRadius: Double): Boolean {
		val dX = x - circleX
		val dY = y - circleY
		val sumRadius = size + circleRadius
		return dX*dX + dY*dY < sumRadius * sumRadius
	}

	fun foodAsChildren(minFood: Double): Int {
		val balance = (food - maxOf(minFood, parentalInvestment))
		return if (balance > 0) (balance / parentalInvestment).toInt() + 1 else 0
	}
}


class Food(
	val x: Double,
	val y: Double,
	val size: Double
)
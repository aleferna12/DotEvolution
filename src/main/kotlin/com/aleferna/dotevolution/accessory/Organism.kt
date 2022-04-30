package com.aleferna.dotevolution.accessory

import javafx.scene.paint.Color
import org.apache.commons.math3.distribution.NormalDistribution
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


object Evolution {
	/* Não confirmado, mas acho que, supondo que cada alelo tem um efeito igual no fenotipo,
	o fenotipo do filho depende somente do quanto do genoma ele herdou do pai X da mae (o que é uma distribuição
	uniforme entre 0 e 1) */
	fun poligenicInheritance(p1_trait: Double, p2_trait: Double): Double {
		val from_f = Random.nextDouble()
		return p1_trait * from_f + p2_trait * (1 - from_f)
	}

	/* Muta um fenótipo presumindo que uma mutação terá muito mais provavelmente
	um efeito pequeno (aproximadamente 0) do que um efeito grande, do que resulta um distribuição normal */
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
}


class Food(
	val x: Double,
	val y: Double,
	val size: Double
)
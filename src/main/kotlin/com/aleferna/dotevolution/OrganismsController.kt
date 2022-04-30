// Sei que o modelo MVC está implementado todo errado mas agr já era

package com.aleferna.dotevolution

import com.aleferna.dotevolution.accessory.*
import com.aleferna.dotevolution.model.Parameters
import com.aleferna.dotevolution.model.StatisticsModel
import com.aleferna.dotevolution.view.MainView
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.scene.paint.Color
import tornadofx.*
import kotlin.math.*
import kotlin.random.Random


class OrganismsController : Controller(){
	val view: MainView by inject()
	val statsModel: StatisticsModel by inject()

	var timePassed: Double = 0.0
	var passingTime = false
	var avgRadius = 0.0
	var avgSpeed = 0.0
	var avgMinfood = 0.0
	var avgTrajectorySpeed = 0.0
	var totalFood = 0

	val organisms = mutableListOf<Organism>()
	val foods = mutableListOf<Food>()

	val updateTimeline = timeline(false) {
		keyFrames += KeyFrame((1.0/ Parameters.FRAMERATE).seconds, EventHandler {
			val delta = Parameters.simSpeed / Parameters.FRAMERATE
			view.drawCanvas.paint(view.canvasBackgroundColor)
			val newOrganisms = mutableListOf<Organism>()
			val deadOrganisms = mutableListOf<Organism>()
			val organismCollisions = mutableListOf<Pair<Organism, Organism>>()
			val foodCollisions = mutableListOf<Pair<Organism, Food>>()
			organisms.forEachIndexed { i, organism ->
				// Update de cada org. RadiusCost é elevado ao cubo porque confere uma vantagem mt grande (também é o volume)
				organism.update(delta, Parameters.radiusCost * Parameters.radiusCost * Parameters.radiusCost, Parameters.speedCost)
				drawOrganism(organism)
				collideWalls(organism, delta)
				// Gets collision between the organisms
				organismCollisions += organisms.slice(i + 1..organisms.lastIndex).mapNotNull { organism2 ->
					if (organism.collideCircle(organism2.x, organism2.y, organism2.size)) { organism to organism2 } else null
				}
				// Kills the organism if it has no food
				if (organism.food < 0.0) { deadOrganisms += organism }
			}
			view.drawGC.fill = Color.SADDLEBROWN
			foods.forEach { food ->
				drawFood(food)
				// Collides each food to a single organism
				// Although food is drawn as a square, it is collided as a circle
				for (organism in organisms) {
					if (
						// Checks tem que ser feitos aqui porque do contrário a colisão seria consumida incorretamente
						organism.food < Parameters.maxFoodAccumulated
						&& organism.age >= Parameters.matureAge
						&& organism.collideCircle(food.x, food.y, food.size)
					) {
						foodCollisions += organism to food
						break
					}
				}
			}
			// Handles collisions
			if (organisms.size + newOrganisms.size - deadOrganisms.size < Parameters.absoluteMaxOrganisms) {
				organismCollisions.forEach {
					if (
						it.first.age >= Parameters.matureAge
						&& it.second.age >= Parameters.matureAge
						&& it.first.food > max(it.first.parentalInvestment, Parameters.minFooodToBreed)
						&& it.second.food > max(it.second.parentalInvestment, Parameters.minFooodToBreed)
						&& hueDifferencePerc(it.first.color.hue, it.second.color.hue) >= Parameters.colorSympatry
					) {
						newOrganisms += breed(it.first, it.second)
					}
				}
			}
			foodCollisions.forEach {
				it.first.food += it.second.size * Parameters.foodDensity
				foods -= it.second
			}
			organisms -= deadOrganisms
			organisms += newOrganisms
			if (organisms.size == 0) { passingTime = false }
		})
		cycleCount = Animation.INDEFINITE
	}
	val foodSpawnTimeline = timeline(false) {
		keyFrames += KeyFrame(0.5.seconds, EventHandler {
			if (foods.size < Parameters.absoluteMaxFood) {
				val howMany = (Parameters.foodBlobsPerSecond /2 * Parameters.simSpeed).roundToInt()
				for (i in 1..minOf(howMany, Parameters.absoluteMaxFood)) {
					foods += Food(
						Random.nextDouble(view.drawCanvas.width),
						Random.nextDouble(view.drawCanvas.height),
						Parameters.foodRadius
					)
				}
			}
		})
		cycleCount = Animation.INDEFINITE
	}
	var infoTimeline = createInfoTimeline(1.0)

	fun createOrganism(x: Double, y: Double) {
		val direction = Random.nextDouble(-PI, PI)
		val trajectory = Parameters.trajectory.sample()
		val invertTrajectory = if (Parameters.bothWaysTrajetory) { Random.nextBoolean() } else false
		val speed = Parameters.speed.sample()
		val radius = Parameters.radius.sample()
		val minFood = Parameters.parentalInvestment.sample()

		val color = Parameters.colorRandomizable.sample()
		organisms += Organism(
			minOf(maxOf(x, view.drawCanvas.layoutX + radius), view.drawCanvas.width - radius),
			minOf(maxOf(y, view.drawCanvas.layoutY + radius), view.drawCanvas.height - radius),
			direction,
			trajectory,
			invertTrajectory,
			speed,
			radius,
			color,
			Parameters.initialFood,
			minFood
		)
	}

	fun breed(organism: Organism, organism2: Organism): Organism {
		val direction = Random.nextDouble(-PI, PI)
		// Randomizes the sign of the trajectory based on the radiubutton
		val invertTrajectory = if (Parameters.bothWaysTrajetory) { Random.nextBoolean() } else false
		val food = organism.parentalInvestment + organism2.parentalInvestment
		organism.food -=  organism.parentalInvestment
		organism2.food -=  organism2.parentalInvestment
		// Transmitindo as características, mutando elas e assegurando que os valores não trespassem os limites
		val trajectory = Parameters.trajectory.normalMutate(
			Evolution.incompleteDominance(
				organism.trajectory,
				organism2.trajectory
			)
		)
		val speed = Parameters.speed.normalMutate(
			Evolution.incompleteDominance(
				organism.speed,
				organism2.speed
			)
		)
		val radius =  Parameters.radius.normalMutate(
			Evolution.incompleteDominance(
				organism.size,
				organism2.size)
		)
		val parentalCare =  Parameters.parentalInvestment.normalMutate(
			Evolution.incompleteDominance(
				organism.parentalInvestment,
				organism2.parentalInvestment
			)
		)
		// Usando uma mutRate menor para as cores não variarem em excesso
		val red = Evolution.normalMutate(
			Evolution.incompleteDominance(organism.color.red, organism2.color.red),
			Parameters.mutationRate * Parameters.colorRandomizable.mutRate
		)
			.coerceIn(0.0, 1.0)
		val green = Evolution.normalMutate(
			Evolution.incompleteDominance(organism.color.green, organism2.color.green),
			Parameters.mutationRate * Parameters.colorRandomizable.mutRate
		)
			.coerceIn(0.0, 1.0)
		val blue = Evolution.normalMutate(
			Evolution.incompleteDominance(organism.color.blue, organism2.color.blue),
			Parameters.mutationRate * Parameters.colorRandomizable.mutRate
		)
			.coerceIn(0.0, 1.0)

		return Organism(
			(organism.x + organism2.x) / 2,
			(organism.y + organism2.y) / 2,
			direction,
			trajectory,
			invertTrajectory,
			speed,
			radius,
			c(red, green, blue),
			food,
			parentalCare
		)
	}

	fun drawOrganism(organism: Organism) {
		view.drawGC.fill = organism.color
		view.drawGC.fillOval(
			organism.x - organism.size,
			organism.y - organism.size,
			organism.size * 2,
			organism.size * 2
		)
	}

	// Draws food as squares
	fun drawFood(food: Food) {
		view.drawGC.fillRect(
			food.x - food.size,
			food.y - food.size,
			food.size * 2,
			food.size * 2
		)
	}

	fun collideWalls(organism: Organism, delta: Double) {
		if (organism.x + organism.speedX*delta - organism.size < 0.0) { organism.x = organism.size }
		else if (organism.x + organism.speedX*delta + organism.size > view.drawCanvas.width) { organism.x = view.drawCanvas.width - organism.size }
		if (organism.y + organism.speedY*delta - organism.size < 0.0) { organism.y = organism.size }
		else if (organism.y + organism.speedY*delta + organism.size > view.drawCanvas.height) { organism.y = view.drawCanvas.height - organism.size }
	}

	fun createInfoTimeline(intervalSeconds: Double): Timeline {
		return timeline(false) {
			keyFrames += KeyFrame(intervalSeconds.seconds, EventHandler {
				if (passingTime) {
					timePassed += Parameters.simSpeed * intervalSeconds
					avgRadius = organisms.map { it.size }.average().round(2)
					avgSpeed = organisms.map { it.speed }.average().round(2)
					avgMinfood = organisms.map { it.parentalInvestment }.average()
					avgTrajectorySpeed = organisms.map { it.trajectory }.average().round(2)
					totalFood = organisms.map { it.food }.sum().toInt()
					runAsync {
						statsModel.appendDataPoint("number_of_organisms", organisms.size)
						statsModel.appendDataPoint("avg_radius", avgRadius)
						statsModel.appendDataPoint("avg_speed", avgSpeed)
						statsModel.appendDataPoint("avg_parental_care", avgMinfood)
						statsModel.appendDataPoint("avg_trajectory_speed", avgTrajectorySpeed)
						statsModel.appendDataPoint("total_food", totalFood)
						statsModel.appendDataPoint("time", timePassed)
					}
				}
			})
			cycleCount = Animation.INDEFINITE
		}
	}
	fun hueDifferencePerc(hue1: Double, hue2: Double): Double {
		var diff = hue1 - hue2
		diff = (diff + 180) % 360 - 180
		return abs(diff / 180)
	}
}
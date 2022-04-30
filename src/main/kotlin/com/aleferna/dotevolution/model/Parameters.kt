package com.aleferna.dotevolution.model

import com.aleferna.dotevolution.accessory.ColorEvolvableParameter
import com.aleferna.dotevolution.accessory.EvolvableParameter
import javafx.beans.property.*
import javafx.scene.paint.Color
import tornadofx.*


// This object holds the validated parameters which can be read by the rest of the classes
object Parameters {
	const val FRAMERATE = 60

	val colorSympatryProperty = SimpleDoubleProperty(0.0)
	var colorSympatry by colorSympatryProperty

	val bothWaysTrajetoryProperty = SimpleBooleanProperty(true)
	var bothWaysTrajetory by bothWaysTrajetoryProperty

	val randomizeColorOnClickProperty = SimpleBooleanProperty(true)
	var randomizeColorOnClick by randomizeColorOnClickProperty

	val generatedPerClickProperty = SimpleIntegerProperty(10)
	var generatedPerClick by generatedPerClickProperty

	val absoluteMaxOrganismsProperty = SimpleIntegerProperty(1000)
	var absoluteMaxOrganisms by absoluteMaxOrganismsProperty

	val matureAgeProperty = SimpleDoubleProperty(1.3)
	var matureAge by matureAgeProperty

	val initialFoodProperty = SimpleDoubleProperty(2000.0)
	var initialFood by initialFoodProperty

	val foodRadiusProperty = SimpleDoubleProperty(5.0)
	var foodRadius by foodRadiusProperty

	// How dense the food is. Effect is multiplying this by the food radius when as Org eats it
	val foodDensityProperty = SimpleDoubleProperty(350.0)
	var foodDensity by foodDensityProperty

	val foodBlobsPerSecondProperty = SimpleDoubleProperty(150.0)
	var foodBlobsPerSecond by foodBlobsPerSecondProperty

	val absoluteMaxFoodProperty = SimpleIntegerProperty(1500)
	var absoluteMaxFood by absoluteMaxFoodProperty

	val mutationRateProperty = SimpleDoubleProperty(1.0)
	var mutationRate by mutationRateProperty

	val minFoodToBreedProperty = SimpleDoubleProperty(500.0)
	var minFooodToBreed by minFoodToBreedProperty

	val maxFoodAccumulatedProperty = SimpleDoubleProperty(10000.0)
	var maxFoodAccumulated by maxFoodAccumulatedProperty

	val statIntervalProperty = SimpleDoubleProperty(2.0)
	var statInterval by statIntervalProperty

	val speedCostProperty = SimpleDoubleProperty(4.0)
	var speedCost by speedCostProperty

	val radiusCostProperty = SimpleDoubleProperty(4.0)
	var radiusCost by radiusCostProperty

	val simSpeedProperty = SimpleDoubleProperty(1.0)
	var simSpeed by simSpeedProperty

	// Evolvable parameters
	// I could wrap these around SimpleObjectProperties but since i never intend to change the objects it would be redundant
	val radius = EvolvableParameter(5.0, 1.0, 0.5)

	val speed = EvolvableParameter(100.0, 20.0, 0.0)

	val parentalInvestment = EvolvableParameter(500.0, 100.0, 10.0)

	val trajectory = EvolvableParameter(0.0, 0.1, 0.0)

	val colorRandomizable = ColorEvolvableParameter(Color.BLACK, 0.1)
}


class ParametersModel(): ViewModel() {
	val colorSympatry = bind(true) { Parameters.colorSympatryProperty }
	val bothWaysTrajectory = bind(true) { Parameters.bothWaysTrajetoryProperty }
	val randomizeColorOnClick = bind(true) { Parameters.randomizeColorOnClickProperty }
	val generatedPerClick = bind(true) { Parameters.generatedPerClickProperty }
	val absoluteMaxOrganisms = bind(true) { Parameters.absoluteMaxOrganismsProperty }
	val matureAge = bind(true) { Parameters.matureAgeProperty }
	val initialFood = bind(true) { Parameters.initialFoodProperty }
	val foodRadius = bind(true) { Parameters.foodRadiusProperty }
	val foodDensity = bind(true) { Parameters.foodDensityProperty }
	val foodBlobsPerSecond = bind(true) { Parameters.foodBlobsPerSecondProperty }
	val absoluteMaxFood = bind(true) { Parameters.absoluteMaxFoodProperty }
	val mutationRate = bind(true) { Parameters.mutationRateProperty }
	val minFoodToBreed = bind(true) { Parameters.minFoodToBreedProperty }
	val maxFoodAccumulated = bind(true) { Parameters.maxFoodAccumulatedProperty }
	val statInterval = bind(true) { Parameters.statIntervalProperty }
	val speedCost = bind(true) { Parameters.speedCostProperty }
	val radiusCost = bind(true) { Parameters.radiusCostProperty }
	val simSpeed = bind(true) { Parameters.simSpeedProperty }
	val radiusMean = bind(true) { Parameters.radius.meanProperty }
	val radiusMutRate = bind(true) { Parameters.radius.mutRateProperty }
	val speedMean = bind(true) { Parameters.speed.meanProperty }
	val speedMutRate = bind(true) { Parameters.speed.mutRateProperty }
	val parentalInvestmentMean = bind(true) { Parameters.parentalInvestment.meanProperty }
	val parentalInvestmentMutRate = bind(true) { Parameters.parentalInvestment.mutRateProperty }
	val trajectoryMean = bind(true) { Parameters.trajectory.meanProperty }
	val trajectoryMutRate = bind(true) { Parameters.trajectory.mutRateProperty }
	val color = bind(true) { Parameters.colorRandomizable.colorProperty }
	val colorMutRate = bind(true) { Parameters.colorRandomizable.mutRateProperty }
}
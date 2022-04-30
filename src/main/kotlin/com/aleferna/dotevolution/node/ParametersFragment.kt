package com.aleferna.dotevolution.node

import com.aleferna.dotevolution.OrganismsController
import com.aleferna.dotevolution.accessory.limitDouble
import com.aleferna.dotevolution.accessory.limitInt
import com.aleferna.dotevolution.accessory.round
import com.aleferna.dotevolution.model.Parameters
import com.aleferna.dotevolution.model.ParametersModel
import com.aleferna.dotevolution.model.StatisticsModel
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.event.EventHandler
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.layout.GridPane
import javafx.scene.layout.Region
import tornadofx.*
import kotlin.math.roundToInt


class ParametersFragment : Fragment() {
	val paramsModel: ParametersModel by inject()
	val controller: OrganismsController by inject()
	val statsModel: StatisticsModel by inject()

	var parameters: Form by singleAssign()
	var evolveGrid: GridPane by singleAssign()
	var colorpick: ColorPicker by singleAssign()
	var bothWaysTrajectory: RadioButton by singleAssign()
	var numberStatLabel: Label by singleAssign()
	var radiusStatLabel: Label by singleAssign()
	var speedStatLabel: Label by singleAssign()
	var parentalInvestmentStatLabel: Label by singleAssign()
	var trajectoryStatLabel: Label by singleAssign()
	var totalFoodStatLabel: Label by singleAssign()
	var timePassedStatLabel: Label by singleAssign()

	var updateStatLabelsTimeline = timeline {
		cycleCount = Animation.INDEFINITE
		keyFrames += KeyFrame(1.5.seconds, EventHandler {
			numberStatLabel.text = controller.organisms.size.toString()
			radiusStatLabel.text = controller.avgRadius.toString()
			speedStatLabel.text = controller.avgSpeed.toString()
			parentalInvestmentStatLabel.text = controller.avgMinfood.round(2).toString()
			trajectoryStatLabel.text = controller.avgTrajectorySpeed.toString()
			totalFoodStatLabel.text = controller.totalFood.toString()
			timePassedStatLabel.text = statsModel.parseTime(controller.timePassed.roundToInt())
		})
	}

	override val root = borderpane {
		parameters = form {
			fieldset("General parameters:") {
				hbox(20) {
					vbox {
						field("Organisms generated:") {
							textfield(paramsModel.generatedPerClick) { limitInt(0) }
						}
						field("Maximum number of organisms:") {
							textfield(paramsModel.absoluteMaxOrganisms).limitInt(0)
						}
						field("Mature age (s):") {
							textfield(paramsModel.matureAge).limitDouble(0)
						}
						field("Required food to breed:") {
							textfield(paramsModel.minFoodToBreed).limitDouble(0)
						}
						field("Max food accumulated:") {
							textfield(paramsModel.maxFoodAccumulated).limitDouble(0)
						}
						// Deprecated
//							field("Max children per encounter:") {
//								textfield(paramsModel.maxChildrenPerEncounter).limitInt(1)
//							}
						field("Mutation rate:") {
							textfield(paramsModel.mutationRate).limitDouble(0)
						}
					}
					vbox {

						field("Organisms initial food supply:") {
							textfield(paramsModel.initialFood).limitDouble(0)
						}
						field("Radius of food blob:") {
							textfield(paramsModel.foodRadius).limitDouble(0)
						}
						field("Food blob density:") {
							textfield(paramsModel.foodDensity).limitDouble(0)
						}
						field("Food blobs generated per second:") {
							textfield(paramsModel.foodBlobsPerSecond).limitDouble(0)
						}
						field("Maximum number of food blobs:") {
							textfield(paramsModel.absoluteMaxFood).limitInt(0)
						}
					}
				}
			}
			// Parâmetros randomizáveis
			// MutRate só afeta os parâmetros no momento da criação, depois isso fica a encargo da mutRate
			fieldset("Evolving parameters:") {
				evolveGrid = gridpane {
					vgap = 10.0
					hgap = 20.0
					row {
						label("Radius:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.radiusMean).limitDouble(Parameters.radius.lowerLimit, Parameters.radius.upperLimit)
						label("Mutation rate:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.radiusMutRate).limitDouble(0)
						label("Metabolic cost:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.radiusCost).limitDouble(0)
					}
					row {
						label("Speed:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.speedMean).limitDouble(Parameters.speed.lowerLimit, Parameters.speed.upperLimit)
						label("Mutation rate:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.speedMutRate).limitDouble(0)
						label("Metabolic cost:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.speedCost).limitDouble(0)
					}
					row {
						label("Parental Investment:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.parentalInvestmentMean).limitDouble(Parameters.parentalInvestment.lowerLimit, Parameters.parentalInvestment.upperLimit)
						label("Mutation rate:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.parentalInvestmentMutRate).limitDouble(0)
					}
					row {
						label("Change in trajectory:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.trajectoryMean).limitDouble()
						label("Mutation rate:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.trajectoryMutRate).limitDouble(Parameters.trajectory.lowerLimit, Parameters.trajectory.upperLimit)
						label("Both ways rotation:") { minWidth = Region.USE_PREF_SIZE }
						bothWaysTrajectory = radiobutton { selectedProperty().bindBidirectional(paramsModel.bothWaysTrajectory) }
					}
					row {
						label("Color:") { minWidth = Region.USE_PREF_SIZE }
						colorpick = colorpicker(
							Parameters.colorRandomizable.color,
							ColorPickerMode.SplitMenuButton
						) { valueProperty().bindBidirectional(paramsModel.color) }
						label("Mutation rate:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.colorMutRate).limitDouble(0)
						label("Sympatric isolation:") { minWidth = Region.USE_PREF_SIZE }
						textfield(paramsModel.colorSympatry).limitDouble(0, 1)
						label("Randomize on click:") { minWidth = Region.USE_PREF_SIZE }
						radiobutton { selectedProperty().bindBidirectional(paramsModel.randomizeColorOnClick) }
					}
				}
			}
			fieldset("Statistics") {
				hbox(20) {
					vbox(10) {
						field("Number of organisms:") {
							numberStatLabel = label()
						}
						field("Average radius:") {
							radiusStatLabel = label()
						}
						field("Average speed:") {
							speedStatLabel = label()
						}
						field("Average parental investment:") {
							parentalInvestmentStatLabel = label()
						}
						field("Change in trajectory:") {
							trajectoryStatLabel = label()
						}
						field("Total food acumulated:") {
							totalFoodStatLabel = label()
						}
						field("Time passed:") {
							timePassedStatLabel = label()
						}
					}
					vbox(10) {
						for (filename in statsModel.filenames - "time") {
							button("Generate graphic") {
								action {
									ChartFragment(
										filename.capitalize().replace('_', ' '),
										statsModel.retrieveData("time"),
										statsModel.retrieveData(filename)
									)
								}
							}
						}
					}
				}
			}
		}
		top = parameters
		bottom = hbox(20) {
			style { padding = box(10.px) }
			label("Simulation Speed:") { minWidth = Region.USE_PREF_SIZE }
			textfield(paramsModel.simSpeed).limitDouble(0)
			label("Time interval to save data (s):") { minWidth = Region.USE_PREF_SIZE }
			textfield(paramsModel.statInterval).limitDouble(0)
			Parameters.statIntervalProperty.addListener { _, _, newV ->
				controller.infoTimeline.stop()
				controller.infoTimeline = controller.createInfoTimeline(newV.toDouble())
				controller.infoTimeline.play()
			}
		}
	}

	init {
		openWindow()
	}
}
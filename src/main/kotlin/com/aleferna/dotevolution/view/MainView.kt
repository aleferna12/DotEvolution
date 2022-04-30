package com.aleferna.dotevolution.view

import com.aleferna.dotevolution.*
import com.aleferna.dotevolution.accessory.paint
import com.aleferna.dotevolution.model.*
import com.aleferna.dotevolution.node.ParametersFragment
import javafx.event.EventHandler
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.*
import javafx.scene.paint.Color
import tornadofx.*
import kotlin.random.Random


class MainView : View("Dot Evolution") {
	val controller: OrganismsController by inject()
	val statsModel: StatisticsModel by inject()
	var drawCanvas: Canvas by singleAssign()
	var drawGC: GraphicsContext by singleAssign()

	val canvasBackgroundColor: Color = Color.ANTIQUEWHITE

	override val root = anchorpane {
		drawCanvas = canvas {
			drawGC = graphicsContext2D
			paint(canvasBackgroundColor)
			onMouseClicked = EventHandler {
				if (!controller.passingTime) {
					statsModel.eraseStatFiles()
					controller.timePassed = 0.0
					controller.passingTime = true
				}
				for (i in 1..minOf(
					Parameters.generatedPerClick,
					Parameters.absoluteMaxOrganisms - controller.organisms.size
				)) {
					controller.createOrganism(it.x, it.y)
				}
				if (Parameters.randomizeColorOnClick) {
					Parameters.colorRandomizable.color =
						c(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
				}
			}
		}
		drawCanvas.widthProperty().bind(widthProperty())
		drawCanvas.heightProperty().bind(heightProperty())

		hbox(10.0) {
			style { padding = box(10.px) }
			AnchorPane.setBottomAnchor(this, 0.0)
			button("Reset simulation") {
				action {
					controller.organisms.clear()
					controller.foods.clear()
					drawCanvas.paint(canvasBackgroundColor)
				}
			}
			button("Settings") {
				action {
					ParametersFragment()
				}
			}
		}
	}

	init {
		controller.updateTimeline.play()
		controller.foodSpawnTimeline.play()
		controller.infoTimeline.play()
	}
}

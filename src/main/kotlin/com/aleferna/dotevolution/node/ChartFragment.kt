package com.aleferna.dotevolution.node

import javafx.scene.chart.NumberAxis
import tornadofx.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.min


class ChartFragment(graphTitle: String, xData: List<Double>, yData: List<Double>) : Fragment() {
	val yAxis = NumberAxis().apply {
		isAutoRanging = false
		lowerBound = if (yData.isEmpty()) 0.0 else floor(yData.min()!!)
		upperBound = if (yData.isEmpty()) 0.0 else ceil(yData.max()!!)
	}
	override val root = linechart(graphTitle, NumberAxis(), yAxis){
		xAxis.label = "seconds"
		isLegendVisible = false
		createSymbols = false
	}

	fun generateGraphic(xData: List<Double>, yData: List<Double>) {
		if (xData.size == yData.size) {
			root.series("") {
				for (i in xData.indices) {
					data(xData[i], yData[i])
				}
			}
		}
	}

	init {
		generateGraphic(xData, yData)
		openWindow()
	}
}
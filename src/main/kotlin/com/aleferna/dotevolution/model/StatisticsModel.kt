package com.aleferna.dotevolution.model

import com.aleferna.dotevolution.accessory.round
import tornadofx.ViewModel
import java.io.File
import java.net.URI


class StatisticsModel : ViewModel() {

	val filenames = listOf(
		"number_of_organisms",
		"avg_radius",
		"avg_speed",
		"avg_parental_care",
		"avg_trajectory_speed",
		"total_food",
		"time"
	)
	val tempDir = System.getProperty("java.io.tmpdir")

	init {
		filenames.forEach { createTempFile(it, "stat", File(tempDir)) }
		eraseStatFiles()
	}

	fun parseTime(time: Int): String {
		return when (time) {
			in 0 until 60 -> "$time s"
			in 60 until 60*60 -> "${(time / 60.0).round(1)} min"
			else -> "${(time / 60.0 / 60.0).round(1)} h"
		}
	}

	fun retrieveData(filename: String): List<Double> {
		val rawData = File(getStatPath(filename)).readText()
		return if (rawData.isNotEmpty()) rawData.subSequence(0, rawData.lastIndex).split(',').map { it.toDouble() } else emptyList()
	}

	fun appendDataPoint(filename: String, dataPoint: Number) {
		File(getStatPath(filename))
			.appendText("$dataPoint,")
	}

	fun getStatPath(filename: String): String {
		return "$tempDir/$filename.stat"
	}

	fun eraseStatFiles() {
		for (filename in filenames) {
			File(getStatPath(filename)).writeText("")
		}
	}
}
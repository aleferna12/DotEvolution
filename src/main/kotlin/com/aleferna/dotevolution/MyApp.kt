package com.aleferna.dotevolution

import com.aleferna.dotevolution.model.StatisticsModel
import com.aleferna.dotevolution.view.MainView
import javafx.stage.Stage
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import java.util.*


class MyApp: App(MainView::class, Styles::class) {
	override fun start(stage: Stage) {
		Locale.setDefault(Locale.US)

		stage.isMaximized = true
		super.start(stage)
	}

	init {
		reloadStylesheetsOnFocus()
	}
}
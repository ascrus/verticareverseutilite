package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.ViewForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class ViewController extends GenerationDropFileMaskObject implements ViewForm {
	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json?.vars?.filter?.views

		this.generationValue(json.create?.views, filter)

		this.fileMask = json.filename?.views
		this.drop = json.drop?.views ?: false
	}

	@Override
	void actionListObject() {
		this.showListObject(ListObjectController.SECTION.VIEWS)
	}
}

package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.GrantsForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class GrantsController extends GenerationObject implements GrantsForm {
	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json.vars?.filter?.grants

		this.generationValue(json.create?.grants, filter)
	}

	@Override
	void actionListObject() {
		this.showListObject(ListObjectController.SECTION.GRANTS)
	}
}

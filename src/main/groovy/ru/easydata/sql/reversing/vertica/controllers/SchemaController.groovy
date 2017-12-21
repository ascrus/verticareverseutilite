package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.SchemaForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class SchemaController extends GenerationDropFileMaskObject implements SchemaForm{
	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}
		String filter = json.vars?.filter?.schemas

		this.generationValue(json.create?.schemas, filter)

		this.fileMask = json.filename?.schemas
		this.drop = json.drop?.schemas ?: false
	}
}

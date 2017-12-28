package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.SqlFunctionForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class SqlFunctionController extends GenerationDropFileMaskObject implements SqlFunctionForm {
	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json.vars?.filter?.sql_functions

		this.generationValue(json.create?.sql_functions, filter)

		this.fileMask = json.filename?.sql_functions
		this.drop = json.drop?.sql_functions ?: false
	}

	@Override
	void actionListObject() {
		this.showListObject(ListObjectController.SECTION.SQL_FUNCTIONS)
	}
}

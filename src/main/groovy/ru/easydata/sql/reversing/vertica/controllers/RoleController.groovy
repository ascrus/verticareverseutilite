package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.RoleForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class RoleController extends GenerationDropFileMaskObject implements RoleForm {
	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json.vars?.filter?.roles

		this.generationValue(json.create?.roles, filter)

		this.fileMask = json.filename?.roles
		this.drop = json.drop?.roles ?: false
	}
}

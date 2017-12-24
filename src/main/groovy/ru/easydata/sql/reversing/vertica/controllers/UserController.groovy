package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.UserForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class UserController extends GenerationDropFileMaskObject implements UserForm {
	@FXML
	private CheckBox checkBoxEmpty

	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void clear() {
		super.clear()
		this.checkBoxEmpty.setSelected(false)
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json?.vars?.filter?.users

		this.empty = json.create?.user_empty ?: false
		this.generationValue(json.create?.users, filter)

		this.fileMask = json.filename?.users
		this.drop = json.drop?.users ?: false
	}

	@Override
	boolean isEmpty() {
		return this.checkBoxEmpty.isSelected()
	}

	@Override
	void setEmpty(boolean isEmpty) {
		this.checkBoxEmpty.setSelected(isEmpty)
	}

	@Override
	void actionListObject() {
		this.showListObject(ListObjectController.SECTION.USERS)
	}
}

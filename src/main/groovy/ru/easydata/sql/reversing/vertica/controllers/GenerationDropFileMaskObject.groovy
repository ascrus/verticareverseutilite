package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import ru.easydata.sql.reversing.vertica.interfaces.forms.DropObject
import ru.easydata.sql.reversing.vertica.interfaces.forms.FileMaskObject

abstract class GenerationDropFileMaskObject extends GenerationObject implements DropObject, FileMaskObject {
	@FXML
	protected TextField textFieldFileMask

	@FXML
	protected CheckBox checkBoxDrop

	@Override
	void clear() {
		super.clear()
		this.checkBoxDrop.setSelected(false)
		this.textFieldFileMask.clear()
	}

	@Override
	boolean isDrop() {
		return this.checkBoxDrop.isSelected()
	}

	@Override
	void setDrop(boolean isDrop) {
		this.checkBoxDrop.setSelected(isDrop)
	}

	@Override
	String getFileMask() {
		return this.textFieldFileMask.getText()
	}

	@Override
	void setFileMask(String fileMask) {
		this.textFieldFileMask.setText(fileMask)
	}
}

package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.SequenceForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class SequenceController extends GenerationDropFileMaskObject implements SequenceForm {
	@FXML
	private CheckBox checkBoxSequencesCurrent

	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void clear() {
		super.clear()
		this.checkBoxSequencesCurrent.setSelected(false)
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		String filter = json.vars?.filter?.sequences

		this.sequencesCurrent = json.create?.sequences_current ?: false
		this.generationValue(json.create?.sequences, filter)

		this.fileMask = json.filename?.sequences
		this.drop = json.drop?.sequences ?: false
	}

	@Override
	boolean isSequencesCurrent() {
		return this.checkBoxSequencesCurrent.isSelected()
	}

	@Override
	void setSequencesCurrent(boolean isSequencesCurrent) {
		this.checkBoxSequencesCurrent.setSelected(isSequencesCurrent)
	}

	@Override
	void actionListObject() {
		this.showListObject(ListObjectController.SECTION.SEQUENCE)
	}
}

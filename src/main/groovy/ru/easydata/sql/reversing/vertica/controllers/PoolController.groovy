package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.stage.Modality
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.configurations.FXMLViewLoader
import ru.easydata.sql.reversing.vertica.interfaces.forms.PoolForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class PoolController extends GenerationDropFileMaskObject implements PoolForm {
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

		String filter = json.vars?.filter?.pools

		this.empty = json.create?.pool_empty ?: false
		this.generationValue(json.create?.pools, filter)


		this.fileMask = json.filename?.pools
		this.drop = json.drop?.pools ?: false
	}

	@Override
	boolean isEmpty() {
		return this.checkBoxEmpty.isSelected()
	}

	@Override
	void setEmpty(boolean isEmpty) {
		this.checkBoxDrop.setSelected(isEmpty)
	}

	void actionListObject() {
		this.listObjectController.setTitle('POOL')
		this.showListObject()
	}
}

package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.Hyperlink
import javafx.scene.control.TextArea
import javafx.stage.Modality
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Autowired
import ru.easydata.sql.reversing.vertica.configurations.FXMLViewLoader


/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
abstract class GenerationObject implements ru.easydata.sql.reversing.vertica.interfaces.forms.GenerationObject {
	@Autowired
	private FXMLViewLoader fxmlViewLoader

	@Autowired
	protected ListObjectController listObjectController

	@FXML
	protected CheckBox checkBoxGeneration

	@FXML
	protected Hyperlink linkListObject

	@FXML
	protected TextArea textAreaFilter

	private Stage stageListObject

	void clear() {
		this.checkBoxGeneration.setSelected(false)
		this.textAreaFilter.clear()
	}

	@Override
	boolean isGeneration() {
		return this.checkBoxGeneration.isSelected()
	}

	@Override
	void setGeneration(boolean isGeneration) {
		this.checkBoxGeneration.setSelected(isGeneration)
	}

	@Override
	String getFilter() {
		return this.textAreaFilter.getText()
	}

	@Override
	void setFilter(String filter) {
		this.textAreaFilter.setText(filter)
	}

	protected void showListObject() {
		Parent root = this.fxmlViewLoader.get('list_object')

		if (this.stageListObject == null) {
			this.stageListObject = new Stage()
			this.stageListObject.setScene(new Scene(root))
			this.stageListObject.setTitle('test')
			this.stageListObject.initModality(Modality.WINDOW_MODAL)
			this.stageListObject.initOwner(this.checkBoxGeneration.getScene().getWindow())

			stageListObject.setOnCloseRequest({e ->
				this.filter = this.listObjectController.getSQL()
			})
			stageListObject.setOnShown({e ->
				this.listObjectController.setSQL(this.filter)
				this.listObjectController.table().getColumns().clear()
			})
		} else {
			this.stageListObject.getScene().setRoot(root)
		}

		this.stageListObject.show()
	}

	protected void generationValue(def value, String filter) {
		if (value instanceof Boolean) {
			this.generation = value as Boolean
		} else if (value instanceof String) {
			this.generation = true
			if (value[0] != '$') {
				this.filter = value
			} else {
				this.filter = filter
			}
		} else {
			this.filter = null
		}
	}
}

package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.TableForm

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class TableController extends GenerationDropFileMaskObject implements TableForm {
	@FXML
	private CheckBox checkBoxConstraints

	@FXML
	private CheckBox checkBoxProjection

	@FXML
	private TextField textFieldKsafe

	@FXML
	void initialize() {
		this.clear()
	}

	@Override
	void clear() {
		super.clear()
		this.checkBoxConstraints.setSelected(false)
		this.checkBoxProjection.setSelected(false)
		this.textFieldKsafe.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		Map c = json.create
		String filter = json.vars?.filter?.tables

		this.constraints = c?.table_constraints ?: false
		this.projection = c?.projection_tables ?: false
		this.projectionKsafe = c?.projection_ksafe
		this.generationValue(c?.tables, filter)

		this.fileMask = json.filename?.tables
		this.drop = json.drop?.tables ?: false
	}

	@Override
	boolean isConstraints() {
		return this.checkBoxConstraints.isSelected()
	}

	@Override
	void setConstraints(boolean isConstraints) {
		this.checkBoxConstraints.setSelected(isConstraints)
	}

	@Override
	boolean isProjection() {
		return this.checkBoxProjection.isSelected()
	}

	@Override
	void setProjection(boolean isProjection) {
		this.checkBoxProjection.setSelected(isProjection)
	}

	@Override
	Integer getProjectionKsafe() {
		return (this.textFieldKsafe.getText() != null && !this.textFieldKsafe.getText().isEmpty()) ? this.textFieldKsafe.getText() as Integer : null
	}

	@Override
	void setProjectionKsafe(Integer projectionKsafe) {
		this.textFieldKsafe.setText(projectionKsafe as String)
	}
}

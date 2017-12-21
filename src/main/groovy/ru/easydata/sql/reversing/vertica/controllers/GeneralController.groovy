package ru.easydata.sql.reversing.vertica.controllers

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import org.springframework.stereotype.Component
import ru.easydata.sql.reversing.vertica.interfaces.forms.GeneralForm
import ru.easydata.sql.reversing.vertica.objects.VarModel

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */
@Component
class GeneralController implements GeneralForm {
	private ObservableList<VarModel> varsData = FXCollections.observableArrayList()

	@FXML
	private TextField textFieldLogFile

	@FXML
	private TableView<VarModel> tableVars

	@FXML
	private TableColumn<VarModel, String> columnVarsName

	@FXML
	private TableColumn<VarModel, String> columnVarsValue

	@FXML
	void initialize() {
		this.clear()

		columnVarsName.setCellValueFactory(new PropertyValueFactory<VarModel,String>("name"))
		columnVarsValue.setCellValueFactory(new PropertyValueFactory<VarModel,String>("value"))

		columnVarsName.setCellFactory(TextFieldTableCell.forTableColumn())
		columnVarsValue.setCellFactory(TextFieldTableCell.forTableColumn())

		columnVarsName.setOnEditCommit({evt ->
			varsData.get(evt.getTablePosition().getRow()).setName(evt.getNewValue())
		})

		columnVarsValue.setOnEditCommit({evt ->
			varsData.get(evt.getTablePosition().getRow()).setValue(evt.getNewValue())
		})

		tableVars.setItems(varsData)
	}


	@Override
	void clear() {
		this.textFieldLogFile.clear()
		this.varsData.clear()
	}

	@Override
	void loadJson(Map json) {
		if (json == null) {
			return
		}

		Map v = json.vars
		Map l = json.log

		if (v != null) {
			List<VarModel> varList = new ArrayList<>()
			v.each() {k ->
				if (k.getValue() instanceof String) {
					varList.add(new VarModel([name: k.getKey(), value: k.getValue()]))
				}
			}
			this.varList = varList
		}

		this.logFile = l?.file
	}

	@FXML
	void contextMenuVarsAction(ActionEvent actionEvent) {
		Object source = actionEvent.getSource()

		if (!source instanceof MenuItem) {
			return
		}

		VarModel var = this.tableVars.getSelectionModel().getSelectedItem()

		MenuItem item = source as MenuItem

		switch (item.getId()) {
			case 'menuItemAdd':
				this.varsData.add(new VarModel([name: 'new name', value: 'new value']))
				break
			case 'menuItemDelete':
				this.tableVars.getItems().remove(var)
				break
		}
	}

	@Override
	String getLogFile() {
		return this.textFieldLogFile.getText()
	}

	@Override
	void setLogFile(String logFile) {
		this.textFieldLogFile.setText(logFile)
	}

	@Override
	List<VarModel> getVarList() {
		return this.varsData
	}

	@Override
	void setVarList(List<VarModel> vars) {
		this.varsData.clear()
		this.varsData.addAll(vars)
	}
}

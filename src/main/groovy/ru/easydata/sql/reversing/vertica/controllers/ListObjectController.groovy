package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import org.springframework.stereotype.Component

@Component
class ListObjectController {
	@FXML
	private Label labelTitle

	@FXML
	private TextArea textAreaSQL

	@FXML
	private TableView tableViewResultSQL

	void setTitle(String title) {
		this.labelTitle.setText(title)
	}

	TableView table() {
		return this.tableViewResultSQL
	}

	String getSQL() {
		return this.textAreaSQL.getText()
	}

	void setSQL(String sql) {
		this.textAreaSQL.setText(sql)
	}

	void butAction() {

	}
}

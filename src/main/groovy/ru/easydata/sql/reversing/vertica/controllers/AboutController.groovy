package ru.easydata.sql.reversing.vertica.controllers

import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.stereotype.Component

import java.awt.Desktop

/**
 * @author Сергей Семыкин
 * @since 29.12.2017
 */
@Component
class AboutController {
	@FXML
	private Label labelTelegram

	@FXML
	private Label labelWeb

	@FXML
	private Label labelVersion

	@FXML
	void initialize() {
		this.labelVersion.setText('0.0.6-SNAPSHOT')

		this.labelWeb.setOnMouseClicked({evt ->
			Desktop.getDesktop().browse(new URL(this.labelWeb.getText()).toURI())
		})

		this.labelTelegram.setOnMouseClicked({evt ->
			Desktop.getDesktop().browse(new URL(this.labelTelegram.getText()).toURI())
		})
	}
}

package ru.easydata.sql.reversing.vertica.controllers

import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

/**
 * @author Сергей Семыкин
 * @since 14.11.2017
 */

@Component
class Dialog {
	@Autowired
	private MessageSource messageSource

	ButtonBar.ButtonData alertClear() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION)
		Locale locale = LocaleContextHolder.getLocale()

		alert.setTitle(this.messageSource.getMessage('app.alert.title.warning', null, locale))
		alert.setHeaderText(this.messageSource.getMessage('app.alert.clear.header', null, locale))

		ButtonType buttonTypeSave = new ButtonType(this.messageSource.getMessage('app.button.yes', null, locale), ButtonBar.ButtonData.YES)
		ButtonType buttonTypeNoSave = new ButtonType(this.messageSource.getMessage('app.button.no', null, locale), ButtonBar.ButtonData.NO)
		ButtonType buttonTypeCancel = new ButtonType(this.messageSource.getMessage('app.button.cancel', null, locale), ButtonBar.ButtonData.CANCEL_CLOSE)

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel)

		Optional<ButtonType> result = alert.showAndWait()

		return result.get().getButtonData()
	}

	ButtonBar.ButtonData alertClose() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION)
		Locale locale = LocaleContextHolder.getLocale()

		alert.setTitle(this.messageSource.getMessage('app.alert.title.warning', null, locale))
		alert.setHeaderText(this.messageSource.getMessage('app.alert.close.header', null, locale))

		ButtonType buttonTypeStop = new ButtonType(this.messageSource.getMessage('app.button.stop', null, locale), ButtonBar.ButtonData.YES)
		ButtonType buttonTypeCancel = new ButtonType(this.messageSource.getMessage('app.button.cancel', null, locale), ButtonBar.ButtonData.CANCEL_CLOSE)

		alert.getButtonTypes().setAll(buttonTypeStop, buttonTypeCancel)

		Optional<ButtonType> result = alert.showAndWait()

		return result.get().getButtonData()
	}

	ButtonBar.ButtonData alertSave() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION)
		Locale locale = LocaleContextHolder.getLocale()

		alert.setTitle(this.messageSource.getMessage('app.alert.title.warning', null, locale))
		alert.setHeaderText(this.messageSource.getMessage('app.alert.save.header', null, locale))

		ButtonType buttonTypeSave = new ButtonType(this.messageSource.getMessage('app.button.save', null, locale), ButtonBar.ButtonData.YES)
		ButtonType buttonTypeNoSave = new ButtonType(this.messageSource.getMessage('app.button.not-save', null, locale), ButtonBar.ButtonData.NO)
		ButtonType buttonTypeCancel = new ButtonType(this.messageSource.getMessage('app.button.cancel', null, locale), ButtonBar.ButtonData.CANCEL_CLOSE)

		alert.getButtonTypes().setAll(buttonTypeSave, buttonTypeNoSave, buttonTypeCancel)

		Optional<ButtonType> result = alert.showAndWait()

		return result.get().getButtonData()
	}

	private File initialDirectory

	File showSaveDialog() {
		Stage stage = new Stage()

		FileChooser fileChooser = new FileChooser()
		fileChooser.setTitle(this.messageSource.getMessage('app.alert.save.title', null, LocaleContextHolder.getLocale()))
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Config Files", ['*.conf']))
		if (initialDirectory != null) {
			fileChooser.setInitialDirectory(initialDirectory)
		}
		File file = fileChooser.showSaveDialog(stage)
		if (file != null) {
			this.initialDirectory = file.getParentFile()
		}
		return file
	}

	File showOpenDialog() {
		Stage stage = new Stage()

		FileChooser fileChooser = new FileChooser()
		fileChooser.setTitle(this.messageSource.getMessage('app.alert.open.title', null, LocaleContextHolder.getLocale()))
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Config Files", ['*.conf']))
		if (initialDirectory != null) {
			fileChooser.setInitialDirectory(initialDirectory)
		}
		File file = fileChooser.showOpenDialog(stage)
		if (file != null) {
			this.initialDirectory = file.getParentFile()
		}
		return file
	}

	File chooserDirectory() {
		Stage stage = new Stage()

		DirectoryChooser directoryChooser = new DirectoryChooser()
		directoryChooser.setTitle(this.messageSource.getMessage('app.directory-chooser', null, LocaleContextHolder.getLocale()))
		return directoryChooser.showDialog(stage)
	}

	File chooserFile() {
		Stage stage = new Stage()

		FileChooser fileChooser = new FileChooser()
		fileChooser.setTitle(this.messageSource.getMessage('app.file-chooser', null, LocaleContextHolder.getLocale()))
		return fileChooser.showOpenDialog(stage)
	}
}
